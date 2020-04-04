package com.qianlei.jiaowu.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.preference.PreferenceManager
import com.google.android.material.navigation.NavigationView
import com.qianlei.jiaowu.R
import com.qianlei.jiaowu.net.StudentClient
import kotlinx.android.synthetic.main.header_layout.*

/**
 * 主界面的activity
 *
 * @author qianlei
 */
class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var mainViewModel: MainViewModel

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
        //设置一句
        val factory = ViewModelProvider.AndroidViewModelFactory(application)
        mainViewModel = factory.create(MainViewModel::class.java)
        mainViewModel.subTitleLiveData.observe(this, Observer { subtitle ->
            if (poemTextView == null) {
                return@Observer
            }
            poemTextView.text = subtitle
        })
    }

    override fun onPause() {
        super.onPause()
        //保存cookies
        StudentClient.saveCookies(this)
    }

    override fun onResume() {
        super.onResume()
        //读取cookies
        StudentClient.readCookies(this)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val needFlush = sharedPreferences.getBoolean(getString(R.string.hitokoto), false)
        if (needFlush) {
            mainViewModel.getSubTitle()
        } else {
            mainViewModel.subTitleLiveData.value = ""
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp()
    }

}