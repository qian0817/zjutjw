package com.qianlei.jiaowu.ui.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.qianlei.jiaowu.R
import com.qianlei.jiaowu.common.Result
import com.qianlei.jiaowu.entity.Student
import com.qianlei.jiaowu.net.StudentApi
import java.util.concurrent.*

/**
 * 主界面的activity
 *
 * @author qianlei
 */
class MainActivity : AppCompatActivity() {
    private val executorService: ExecutorService = ThreadPoolExecutor(1, 1, 0,
            TimeUnit.SECONDS, SynchronousQueue(), ThreadFactory { r: Runnable? -> Thread(r, "获取学生信息线程") }, ThreadPoolExecutor.DiscardPolicy())
    private val handler = Handler()
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var localBroadcastManager: LocalBroadcastManager
    private lateinit var loginReceiver: LoginReceiver
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
        localBroadcastManager = LocalBroadcastManager.getInstance(this.applicationContext)
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.qianlei.jiaowu.LOGIN_BROADCAST")
        loginReceiver = LoginReceiver()
        localBroadcastManager.registerReceiver(loginReceiver, intentFilter)
        localBroadcastManager.sendBroadcast(Intent("com.qianlei.jiaowu.LOGIN_BROADCAST"))
    }

    override fun onPause() {
        super.onPause()
        StudentApi.getStudentApi(this).saveCookies()
    }

    override fun onDestroy() {
        super.onDestroy()
        localBroadcastManager.unregisterReceiver(loginReceiver)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp()
    }

    private inner class LoginReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            executorService.submit {
                val result: Result<Student> = StudentApi.getStudentApi(context).getStudentInformation()
                if (result.isSuccess()) {
                    handler.post {
                        val navView = findViewById<NavigationView>(R.id.nav_view)
                        val header = navView.getHeaderView(0)
                        val studentNameTextView = header.findViewById<TextView>(R.id.student_name_text)
                        val studentIdTextView = header.findViewById<TextView>(R.id.student_id_text)
                        studentIdTextView.text = result.data!!.studentId
                        studentNameTextView.text = result.data!!.name
                    }
                }
            }
        }
    }
}