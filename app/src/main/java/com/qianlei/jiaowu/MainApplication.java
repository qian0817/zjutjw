package com.qianlei.jiaowu;

import android.app.Application;
import android.widget.Toast;

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

    private static MainApplication instance;

    public static MainApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(new CrashExceptionHandler());
        instance = this;
    }

    class CrashExceptionHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
            Toast.makeText(getApplicationContext(), "程序出现异常，即将退出", Toast.LENGTH_SHORT).show();
            if (getExternalCacheDir() != null) {
                String fileName = getExternalCacheDir().getAbsolutePath() + File.separator + "error.log";
                try (PrintStream printStream = new PrintStream(new FileOutputStream(fileName, true))) {
                    String date = SimpleDateFormat.getDateTimeInstance().format(new Date());
                    printStream.println(date + " [" + t.getName() + "]");
                    e.printStackTrace(printStream);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }
}
