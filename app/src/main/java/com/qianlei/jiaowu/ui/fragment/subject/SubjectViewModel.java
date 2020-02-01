package com.qianlei.jiaowu.ui.fragment.subject;

import android.os.Handler;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.qianlei.jiaowu.common.Result;
import com.qianlei.jiaowu.core.net.StudentApi;
import com.qianlei.jiaowu.entity.Subject;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author qianlei
 */
public class SubjectViewModel extends ViewModel {
    private MutableLiveData<Result<List<Subject>>> mutableLiveData = new MutableLiveData<>();
    private StudentApi studentApi = StudentApi.getStudentApi();
    private ExecutorService executor = new ThreadPoolExecutor(1, 1,
            0, TimeUnit.SECONDS, new SynchronousQueue<>(), r -> new Thread(r, "获取课程进程"),
            new ThreadPoolExecutor.DiscardPolicy());
    private Handler handler = new Handler();

    void changeTerm(String year, String term) {
        executor.execute(() -> {
            Result<List<Subject>> result = studentApi.getStudentTimetable(year, term);
            handler.post(() -> mutableLiveData.setValue(result));
        });
    }

    MutableLiveData<Result<List<Subject>>> getResult() {
        return mutableLiveData;
    }
}
