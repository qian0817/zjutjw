package com.qianlei.jiaowu.ui.fragment.login

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.qianlei.jiaowu.R
import com.qianlei.jiaowu.common.Result
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * 登录的Fragment,用于学生登录
 *
 * @author qianlei
 */
class LoginFragment : Fragment() {

    companion object {
        private const val ID = "id"
        private const val PASSWORD = "password"
        private const val USER = "user"
    }

    private val mViewModel by viewModels<LoginViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.captcha.observe(this.viewLifecycleOwner, Observer { changeCaptcha(it) })
        mViewModel.loginResult.observe(this.viewLifecycleOwner, Observer { login(it) })
        loginButton.setOnClickListener {
            loginButton.startAnimation()
            val studentId = studentIdEditText.text.toString()
            val password = passwordEditText.text.toString()
            val captcha = captchaText.text.toString()
            mViewModel.login(studentId, password, captcha)
        }
        captchaImage.setOnClickListener { mViewModel.changeCaptcha() }
        //获取之前填写的学号和密码
        getRememberPassword()
        mViewModel.changeCaptcha()
    }

    /**
     * 获取之前保存的密码
     */
    private fun getRememberPassword() {
        val preferences = createEncryptedSharedPreferences(context ?: return)
        val studentId = preferences.getString(ID, "")
        val password = preferences.getString(PASSWORD, "")
        passwordEditText.setText(password)
        studentIdEditText.setText(studentId)
    }

    /**
     * 更换验证码图片,将其修改为[result]中的图片
     */
    private fun changeCaptcha(result: Result<Bitmap>) {
        if (result.isSuccess()) {
            captchaImage.setImageBitmap(result.data)
        } else {
            Toast.makeText(context, result.msg, Toast.LENGTH_SHORT).show()
        }
    }

    private fun login(result: Result<String>) {
        val studentId = studentIdEditText.text.toString()
        val password = passwordEditText.text.toString()
        if (result.isSuccess()) { //保存用户名 密码
            val preferences = createEncryptedSharedPreferences(context ?: return)
            val editor = preferences.edit()
            editor.putString(ID, studentId)
            editor.putString(PASSWORD, password)
            editor.apply()
            //跳转到课程界面
            val controller = Navigation.findNavController(view ?: return)
            controller.navigate(R.id.action_navigation_login_to_navigation_lesson)
        } else {
            mViewModel.changeCaptcha()
            captchaText.setText("")
            Toast.makeText(context, result.msg, Toast.LENGTH_SHORT).show()
        }
        loginButton.revertAnimation()
    }

    /**
     * 创建可加密的密码
     */
    private fun createEncryptedSharedPreferences(context: Context): SharedPreferences {
        val key = MasterKey
            .Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        return EncryptedSharedPreferences.create(
            context,
            USER,
            key,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

}