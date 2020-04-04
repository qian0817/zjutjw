package com.qianlei.jiaowu.ui.fragment.login

import android.app.Application
import android.graphics.Bitmap
import android.os.Handler
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.qianlei.jiaowu.common.Result
import com.qianlei.jiaowu.net.StudentClient
import java.util.concurrent.*

/**
 * @author qianlei
 */
class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val executor: ExecutorService = ThreadPoolExecutor(1, 1,
            0, TimeUnit.SECONDS, SynchronousQueue(), ThreadFactory { r: Runnable? -> Thread(r, "登录获取验证码线程") }
            , ThreadPoolExecutor.DiscardPolicy())
    val loginResult = MutableLiveData<Result<String>>()
    val captcha = MutableLiveData<Result<Bitmap>>()
    private val handler = Handler()
    fun login(studentId: String?, password: String?, captcha: String?) {
        executor.submit {
            val result = StudentClient.login(getApplication(), studentId, password, captcha)
            handler.post { loginResult.setValue(result) }
        }
    }

    fun changeCaptcha() {
        executor.submit {
            val result = StudentClient.getCaptchaImage(getApplication())
            handler.post { captcha.setValue(result) }
        }
    }

}