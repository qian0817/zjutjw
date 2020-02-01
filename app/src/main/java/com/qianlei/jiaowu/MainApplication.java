package com.qianlei.jiaowu;

import android.app.Application;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author qianlei
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //设置错误日志
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
    }

    private static class ExceptionHandler implements Thread.UncaughtExceptionHandler {
        private Application application;

        ExceptionHandler(Application application) {
            this.application = application;
        }

        @Override
        public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
            if (application.getExternalCacheDir() != null) {
                String fileName = application.getExternalCacheDir().getAbsolutePath() + File.separator + "error.log";
                try (PrintStream printStream = new PrintStream(new FileOutputStream(fileName, true))) {
                    String date = SimpleDateFormat.getDateTimeInstance().format(new Date());
                    printStream.println(date + " [" + t.getName() + "]");
                    e.printStackTrace(printStream);
                    android.os.Process.killProcess(android.os.Process.myPid());
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
