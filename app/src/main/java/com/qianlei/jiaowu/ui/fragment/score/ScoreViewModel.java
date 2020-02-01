package com.qianlei.jiaowu.ui.fragment.score;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.qianlei.jiaowu.common.Result;
import com.qianlei.jiaowu.common.ResultType;
import com.qianlei.jiaowu.core.db.MyDataBase;
import com.qianlei.jiaowu.core.db.dao.ScoreDao;
import com.qianlei.jiaowu.core.net.StudentApi;
import com.qianlei.jiaowu.entity.Score;

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

    private final StudentApi studentApi = StudentApi.getStudentApi();
    private ExecutorService executor = new ThreadPoolExecutor(1, 1,
            0, TimeUnit.SECONDS, new SynchronousQueue<>(), r -> new Thread(r, "获取成绩线程"),
            new ThreadPoolExecutor.DiscardPolicy());
    private Handler handler = new Handler();

    private MutableLiveData<Result<List<Score>>> result = new MutableLiveData<>();
    private Application application;
    private boolean getFromDatabase = true;

    public ScoreViewModel(@NonNull Application application) {
        super(application);
        this.application = application;

    }

    MutableLiveData<Result<List<Score>>> getResult() {
        return result;
    }

    void changeTerm(String year, String term) {
        executor.execute(() -> {
            if (getFromDatabase) {
                getFromDatabase = false;
                Result<List<Score>> score = getDataFromDatabase(year, term);
                if (score.isSuccess()) {
                    handler.post(() -> result.setValue(score));
                    return;
                }
            }
            Result<List<Score>> score = getDataFromNet(year, term);
            handler.post(() -> result.setValue(score));
        });

    }

    private Result<List<Score>> getDataFromDatabase(String year, String term) {
        Log.d("score", "从数据库中获取考试信息");
        ScoreDao scoreDao = MyDataBase.getDatabase(application).getScoreDao();
        List<Score> scoreList = scoreDao.selectAllSubjectByYearAndTerm(year, term);
        if (scoreList != null && !scoreList.isEmpty()) {
            return new Result<>(ResultType.OK, "从数据获取成功", scoreList);
        } else {
            Log.d("score", "数据库中无数据");
            return new Result<>(ResultType.OTHER, "数据库中无数据");
        }
    }

    @NotNull
    private Result<List<Score>> getDataFromNet(String year, String term) {
        Log.d("score", "从网络中获取考试信息");
        Result<List<Score>> result = studentApi.getStudentScore(year, term);
        if (result.isSuccess()) {
            //向数据库中添加数据
            ScoreDao scoreDao = MyDataBase.getDatabase(application).getScoreDao();
            List<Score> scoreList = result.getData();
            scoreDao.deleteAllSubjectByYearAndTerm(year, term);
            for (Score score : scoreList) {
                scoreDao.insertSubject(score);
            }
        }
        return result;
    }
}
