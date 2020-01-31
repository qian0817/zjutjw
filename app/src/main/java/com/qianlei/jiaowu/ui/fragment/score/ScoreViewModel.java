package com.qianlei.jiaowu.ui.fragment.score;

import android.os.Handler;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.qianlei.jiaowu.common.Result;
import com.qianlei.jiaowu.core.net.StudentApi;
import com.qianlei.jiaowu.entity.Score;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author qianlei
 */
public class ScoreViewModel extends ViewModel {

    private final StudentApi studentApi = StudentApi.getStudentApi();
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private MutableLiveData<Result<List<Score>>> result = new MutableLiveData<>();
    private Handler handler = new Handler();

    void changeTerm(String year, String term) {
        executor.execute(() -> {
            Result<List<Score>> score = studentApi.getStudentScore(year, term);
            handler.post(() -> result.setValue(score));
        });

    }

    MutableLiveData<Result<List<Score>>> getResult() {
        return result;
    }
}
