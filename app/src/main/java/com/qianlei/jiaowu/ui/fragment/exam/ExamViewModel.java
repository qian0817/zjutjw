package com.qianlei.jiaowu.ui.fragment.exam;

import android.os.Handler;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.qianlei.jiaowu.common.Result;
import com.qianlei.jiaowu.core.net.StudentApi;
import com.qianlei.jiaowu.entity.Examination;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 考试信息相关的ViewModel
 *
 * @author qianlei
 */
public class ExamViewModel extends ViewModel {
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private StudentApi studentApi = StudentApi.getStudentApi();
    private Handler handler = new Handler();

    private MutableLiveData<Result<List<Examination>>> examListData = new MutableLiveData<>();

    public ExamViewModel() {
    }

    MutableLiveData<Result<List<Examination>>> getExamListData() {
        return examListData;
    }

    void changeTerm(String year, String term) {
        executorService.submit(() -> {
            Result<List<Examination>> result = studentApi.getStudentExamInformation(year, term);
            handler.post(() -> examListData.setValue(result));
        });


    }
}