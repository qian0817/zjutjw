package com.qianlei.jiaowu.ui.activity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.qianlei.jiaowu.R
import com.qianlei.jiaowu.common.Result
import com.qianlei.jiaowu.entity.Student
import com.qianlei.jiaowu.net.StudentApi
import com.qianlei.jiaowu.repository.StudentRepository

/**
 * 主界面的activity
 *
 * @author qianlei
 */
class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView = findViewById<NavigationView>(R.id.nav_view)
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        appBarConfiguration = AppBarConfiguration.Builder(
                R.id.navigation_examination, R.id.navigation_login,
                R.id.navigation_score, R.id.navigation_subject, R.id.navigation_setting)
                .setDrawerLayout(drawer).build()
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(navView, navController)
        //当学生信息修改时，更新学生数据
        StudentRepository.studentData.observe(this, Observer { result: Result<Student> -> updateStudentInformation(result) })
        //先尝试获取学生信息
        val task = StudentRepository.GetStudentInformationTask()
        task.execute()
    }

    /**
     * 更新学生信息
     */
    private fun updateStudentInformation(result: Result<Student>) {
        val navView = findViewById<NavigationView>(R.id.nav_view)
        val data = result.data ?: return
        val header = navView.getHeaderView(0)
        val studentNameTextView = header.findViewById<TextView>(R.id.student_name_text)
        val studentIdTextView = header.findViewById<TextView>(R.id.student_id_text)
        studentIdTextView.text = data.studentId
        studentNameTextView.text = data.name
    }

    override fun onPause() {
        super.onPause()
        //保存cookies
        StudentApi.getStudentApi(this).saveCookies()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp()
    }

}