package com.qianlei.jiaowu.ui.fragment.exam;

import android.app.Application;
import android.os.Handler;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.qianlei.jiaowu.common.Result;
import com.qianlei.jiaowu.common.ResultType;
import com.qianlei.jiaowu.core.db.MyDataBase;
import com.qianlei.jiaowu.core.db.dao.ExamDao;
import com.qianlei.jiaowu.core.net.StudentApi;
import com.qianlei.jiaowu.entity.Examination;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 考试信息相关的ViewModel
 *
 * @author qianlei
 */
public class ExamViewModel extends AndroidViewModel {
    private ExecutorService executorService = new ThreadPoolExecutor(1, 1,
            0, TimeUnit.MINUTES, new SynchronousQueue<>(), r -> new Thread(r, "获取考试信息线程"),
            new ThreadPoolExecutor.DiscardPolicy());
    private Handler handler = new Handler();
    private boolean getDataFromDataBase = true;
    private ExamDao examDao;
    private StudentApi studentApi;
    private Application application;

    private MutableLiveData<Result<List<Examination>>> examListData = new MutableLiveData<>();

    public ExamViewModel(Application application) {
        super(application);
        this.application = application;
        studentApi = StudentApi.getStudentApi(application);
        examDao = MyDataBase.getDatabase(application).getExamDao();
    }

    MutableLiveData<Result<List<Examination>>> getExamListData() {
        return examListData;
    }

    /**
     * 刷新数据
     *
     * @param year 学年
     * @param term 学期
     */
    void refreshData(String year, String term) {
        executorService.submit(() -> {
            //首次从本地数据库中获取数据 之后从网络中获取数据
            if (getDataFromDataBase) {
                getDataFromDataBase = false;
                final Result<List<Examination>> result = getDataFromDatabase(year, term);
                if (result.isSuccess()) {
                    handler.post(() -> examListData.setValue(result));
                    return;
                }
            }
            Result<List<Examination>> result = getDataFromNet(year, term);
            handler.post(() -> examListData.setValue(result));
        });
    }

    /**
     * 修改学期
     *
     * @param year 修改好的学年
     * @param term 修改后的学期
     */
    void changeTerm(String year, String term) {
        getDataFromDataBase = true;
        refreshData(year, term);
    }

    @NotNull
    private Result<List<Examination>> getDataFromNet(String year, String term) {
        Result<List<Examination>> result = studentApi.getStudentExamInformation(year, term);
        if (result.isSuccess()) {
            //向数据库中添加数据
            ExamDao examDao = MyDataBase.getDatabase(application).getExamDao();
            List<Examination> examinationList = result.getData();
            examDao.deleteAllByYearAndTerm(year, term);
            for (Examination examination : examinationList) {
                examDao.insertExam(examination);
            }
        }
        return result;
    }

    private Result<List<Examination>> getDataFromDatabase(String year, String term) {
        List<Examination> examinationList = examDao.selectAllExamByYearAndTerm(year, term);
        if (examinationList != null && !examinationList.isEmpty()) {
            return new Result<>(ResultType.OK, "从数据获取成功", examinationList);
        } else {
            return new Result<>(ResultType.OTHER, "数据库中无数据");
        }
    }
}