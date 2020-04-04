package com.qianlei.jiaowu.ui.fragment.login

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.qianlei.jiaowu.common.Result
import com.qianlei.jiaowu.net.StudentClient
import kotlinx.coroutines.launch

/**
 * @author qianlei
 */
class LoginViewModel(application: Application) : AndroidViewModel(application) {
    val loginResult = MutableLiveData<Result<String>>()
    val captcha = MutableLiveData<Result<Bitmap>>()

    fun login(studentId: String?, password: String?, captcha: String?) {
        viewModelScope.launch {
            loginResult.value = StudentClient.login(getApplication(), studentId, password, captcha)
        }
    }

    fun changeCaptcha() {
        viewModelScope.launch {
            captcha.value = StudentClient.getCaptchaImage(getApplication())
        }
    }

}