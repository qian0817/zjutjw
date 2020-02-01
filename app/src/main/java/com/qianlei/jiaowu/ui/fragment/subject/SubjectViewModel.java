package com.qianlei.jiaowu.ui.fragment.subject;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.qianlei.jiaowu.common.Result;
import com.qianlei.jiaowu.common.ResultType;
import com.qianlei.jiaowu.core.db.MyDataBase;
import com.qianlei.jiaowu.core.db.dao.SubjectDao;
import com.qianlei.jiaowu.core.net.StudentApi;
import com.qianlei.jiaowu.entity.Subject;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author qianlei
 */
public class SubjectViewModel extends AndroidViewModel {
    private StudentApi studentApi = StudentApi.getStudentApi();
    private ExecutorService executor = new ThreadPoolExecutor(1, 1,
            0, TimeUnit.SECONDS, new SynchronousQueue<>(), r -> new Thread(r, "获取课程进程"),
            new ThreadPoolExecutor.DiscardPolicy());
    private Handler handler = new Handler();

    private MutableLiveData<Result<List<Subject>>> mutableLiveData = new MutableLiveData<>();
    private boolean getFromDatabase = true;
    private Application application;

    public SubjectViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }


    MutableLiveData<Result<List<Subject>>> getResult() {
        return mutableLiveData;
    }

    void changeTerm(String year, String term) {
        executor.execute(() -> {
            if (getFromDatabase) {
                getFromDatabase = false;
                Result<List<Subject>> subject = getDataFromDatabase(year, term);
                if (subject.isSuccess()) {
                    handler.post(() -> mutableLiveData.setValue(subject));
                    return;
                }
            }
            Result<List<Subject>> result = getDataFromNet(year, term);
            handler.post(() -> mutableLiveData.setValue(result));
        });
    }

    private Result<List<Subject>> getDataFromDatabase(String year, String term) {
        Log.d("subject", "从数据库中获取考试信息");
        SubjectDao subjectDao = MyDataBase.getDatabase(application).getSubjectDao();
        List<Subject> subjectList = subjectDao.selectAllSubjectByYearAndTerm(year, term);
        System.out.println(subjectList);
        if (subjectList != null && !subjectList.isEmpty()) {
            return new Result<>(ResultType.OK, "从数据获取成功", subjectList);
        } else {
            return new Result<>(ResultType.OTHER, "数据库中无数据");
        }
    }

    @NotNull
    private Result<List<Subject>> getDataFromNet(String year, String term) {
        Log.d("subject", "从网络中获取考试信息");
        Result<List<Subject>> result = studentApi.getStudentTimetable(year, term);
        if (result.isSuccess()) {
            //向数据库中添加数据
            SubjectDao subjectDao = MyDataBase.getDatabase(application).getSubjectDao();
            List<Subject> subjectList = result.getData();
            subjectDao.deleteAllSubjectByYearAndTerm(year, term);
            for (Subject subject : subjectList) {
                subjectDao.insertSubject(subject);
            }
        }
        return result;
    }
}
