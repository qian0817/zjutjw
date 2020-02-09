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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.navigation.Navigation
import com.qianlei.jiaowu.MainApplication
import com.qianlei.jiaowu.R
import com.qianlei.jiaowu.common.Result
import com.qianlei.jiaowu.repository.StudentRepository
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

    private lateinit var mViewModel: LoginViewModel
    private lateinit var preferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_login, container, false)
        val factory = AndroidViewModelFactory(MainApplication.getInstance())
        mViewModel = factory.create(LoginViewModel::class.java)
        preferences = MainApplication.getInstance().getSharedPreferences(USER, Context.MODE_PRIVATE)
        return root
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
        //获取之前填写的学号和密码
        getRememberPassword()
        mViewModel.changeCaptcha()
    }

    private fun getRememberPassword() {
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
            val editor = preferences.edit()
            editor.putString(ID, studentId)
            editor.putString(PASSWORD, password)
            editor.apply()
            //获取登录学生的信息
            val task = StudentRepository.GetStudentInformationTask()
            task.execute()
            //跳转到课程界面
            val v = view
            if (v != null) {
                val controller = Navigation.findNavController(v.rootView)
                controller.navigate(R.id.action_navigation_login_to_navigation_lesson)
            }
        } else {
            mViewModel.changeCaptcha()
            captchaText.setText("")
            Toast.makeText(context, result.msg, Toast.LENGTH_SHORT).show()
        }
    }
}