package com.qianlei.jiaowu.ui.fragment.score;

import android.app.Application;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.qianlei.jiaowu.common.Result;
import com.qianlei.jiaowu.common.ResultType;
import com.qianlei.jiaowu.db.MyDataBase;
import com.qianlei.jiaowu.db.dao.ScoreDao;
import com.qianlei.jiaowu.entity.Score;
import com.qianlei.jiaowu.net.StudentApi;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author qianlei
 */
public class ScoreViewModel extends AndroidViewModel {

    private ExecutorService executor = new ThreadPoolExecutor(1, 1,
            0, TimeUnit.SECONDS, new SynchronousQueue<>(), r -> new Thread(r, "获取成绩线程"),
            new ThreadPoolExecutor.DiscardPolicy());
    private Handler handler = new Handler();

    private MutableLiveData<Result<List<Score>>> result = new MutableLiveData<>();
    private final StudentApi studentApi;
    private ScoreDao scoreDao;
    private Application application;
    private boolean getFromDatabase = true;

    public ScoreViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        studentApi = StudentApi.getStudentApi(application);
        scoreDao = MyDataBase.getDatabase(application).getScoreDao();
    }

    MutableLiveData<Result<List<Score>>> getResult() {
        return result;
    }

    /**
     * 刷新数据
     *
     * @param year 学年
     * @param term 学期
     */
    void refreshData(String year, String term) {
        executor.execute(() -> {
            if (getFromDatabase) {
                getFromDatabase = false;
                Result<List<Score>> score = getDataFromDatabase(year, term);
                //如果数据库中有数据则修改数据，否则从网络中获取
                if (score.isSuccess()) {
                    handler.post(() -> result.setValue(score));
                    return;
                }
            }
            Result<List<Score>> score = getDataFromNet(year, term);
            handler.post(() -> result.setValue(score));
        });
    }

    /**
     * 修改学期
     *
     * @param year 学年
     * @param term 学期
     */
    void changeTerm(String year, String term) {
        //第一次先从数据中获取数据
        getFromDatabase = true;
        refreshData(year, term);
    }

    private Result<List<Score>> getDataFromDatabase(String year, String term) {
        scoreDao = MyDataBase.getDatabase(application).getScoreDao();
        List<Score> scoreList = scoreDao.selectAllScoreByYearAndTerm(year, term);
        if (scoreList != null && !scoreList.isEmpty()) {
            return new Result<>(ResultType.OK, "从数据获取成功", scoreList);
        } else {
            return new Result<>(ResultType.OTHER, "数据库中无数据");
        }
    }

    @NotNull
    private Result<List<Score>> getDataFromNet(String year, String term) {
        Result<List<Score>> result = studentApi.getStudentScore(year, term);
        if (result.isSuccess()) {
            //向数据库中添加数据
            scoreDao = MyDataBase.getDatabase(application).getScoreDao();
            List<Score> scoreList = result.getData();
            scoreDao.deleteAllScoreByYearAndTerm(year, term);
            for (Score score : scoreList) {
                scoreDao.insertScore(score);
            }
        }
        return result;
    }
}
