package com.qianlei.jiaowu.ui.fragment.login

import android.content.Context
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
import com.qianlei.jiaowu.R
import com.qianlei.jiaowu.common.Result
import kotlinx.android.synthetic.main.fragment_login.*

/**
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
        mViewModel.captcha.observe(this.viewLifecycleOwner, Observer { result: Result<Bitmap> -> changeCaptcha(result) })
        mViewModel.loginResult.observe(this.viewLifecycleOwner, Observer { result: Result<String> -> login(result) })
        loginButton.setOnClickListener {
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

    private fun getRememberPassword() {
        val c = context ?: return
        val preferences = c.getSharedPreferences(USER, Context.MODE_PRIVATE)
        val studentId = preferences.getString(ID, "")
        val password = preferences.getString(PASSWORD, "")
        passwordEditText.setText(password)
        studentIdEditText.setText(studentId)
    }

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
            val c = context ?: return
            val preferences = c.getSharedPreferences(USER, Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putString(ID, studentId)
            editor.putString(PASSWORD, password)
            editor.apply()
            //跳转到课程界面
            val v = view ?: return
            val controller = Navigation.findNavController(v)
            controller.navigate(R.id.action_navigation_login_to_navigation_lesson)
        } else {
            mViewModel.changeCaptcha()
            captchaText.setText("")
            Toast.makeText(context, result.msg, Toast.LENGTH_SHORT).show()
        }
    }
}