package com.qianlei.jiaowu.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.qianlei.jiaowu.R;
import com.qianlei.jiaowu.common.Result;
import com.qianlei.jiaowu.core.net.StudentApi;
import com.qianlei.jiaowu.entity.Student;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 主界面的activity
 *
 * @author qianlei
 */
public class MainActivity extends AppCompatActivity {
    private ExecutorService executorService = new ThreadPoolExecutor(1, 1, 0,
            TimeUnit.SECONDS, new SynchronousQueue<>(), r -> new Thread(r, "获取学生信息线程"), new ThreadPoolExecutor.DiscardPolicy());
    private Handler handler = new Handler();
    private AppBarConfiguration appBarConfiguration;
    private LocalBroadcastManager localBroadcastManager;
    private LoginReceiver loginReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationView navView = findViewById(R.id.nav_view);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_examination, R.id.navigation_login,
                R.id.navigation_score, R.id.navigation_subject)
                .setDrawerLayout(drawer).build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        localBroadcastManager = LocalBroadcastManager.getInstance(this.getApplicationContext());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.qianlei.jiaowu.LOGIN_BROADCAST");
        loginReceiver = new LoginReceiver();
        localBroadcastManager.registerReceiver(loginReceiver, intentFilter);

        localBroadcastManager.sendBroadcast(new Intent("com.qianlei.jiaowu.LOGIN_BROADCAST"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        StudentApi.getStudentApi(this).saveCookies();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(loginReceiver);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    private class LoginReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            executorService.submit(() -> {
                Result<Student> result = StudentApi.getStudentApi(context).getStudentInformation();
                if (result.isSuccess()) {
                    handler.post(() -> {
                        NavigationView navView = findViewById(R.id.nav_view);
                        View header = navView.getHeaderView(0);
                        TextView studentNameTextView = header.findViewById(R.id.student_name_text);
                        TextView studentIdTextView = header.findViewById(R.id.student_id_text);

                        studentIdTextView.setText(result.getData().getStudentId());
                        studentNameTextView.setText(result.getData().getName());
                    });
                }
            });
        }
    }
}
