package com.qianlei.jiaowu.ui.fragment.login;

import android.graphics.Bitmap;
import android.os.Handler;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.qianlei.jiaowu.common.Result;
import com.qianlei.jiaowu.core.net.StudentApi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author qianlei
 */
public class LoginViewModel extends ViewModel {

    private StudentApi studentApi = StudentApi.getStudentApi();
    private ExecutorService executor = new ThreadPoolExecutor(1, 1,
            0, TimeUnit.SECONDS, new SynchronousQueue<>(), r -> new Thread(r, "登录获取验证码线程")
            , new ThreadPoolExecutor.DiscardPolicy());
    private MutableLiveData<Result<String>> loginResult = new MutableLiveData<>();
    private MutableLiveData<Result<Bitmap>> captcha = new MutableLiveData<>();
    private Handler handler = new Handler();

    void login(String studentId, String password, String captcha) {
        executor.submit(() -> {
            final Result<String> result = studentApi.login(studentId, password, captcha);
            System.out.println(result);
            handler.post(() -> loginResult.setValue(result));
        });
    }

    void changeCaptcha() {
        executor.submit(() -> {
            final Result<Bitmap> result = studentApi.getCaptchaImage();
            handler.post(() -> captcha.setValue(result));
        });
    }

    MutableLiveData<Result<String>> getLoginResult() {
        return loginResult;
    }

    MutableLiveData<Result<Bitmap>> getCaptcha() {
        return captcha;
    }
}
