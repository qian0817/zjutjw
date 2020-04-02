package com.qianlei.jiaowu

import android.app.Application
import android.content.Intent
import android.os.Process
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.PrintStream
import java.text.SimpleDateFormat
import java.util.*


/**
 * @author qianlei
 */
@Suppress("unused")
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler(CrashExceptionHandler())
    }

    /**
     * 用于未处理异常处理
     */
    internal inner class CrashExceptionHandler : Thread.UncaughtExceptionHandler {
        override fun uncaughtException(t: Thread, e: Throwable) {
            //记录异常信息
            val dir = externalCacheDir
            if (dir != null) {
                val fileName = dir.absolutePath + File.separator + "error.log"
                val file = File(fileName)
                //日志文件大于10M则删除
                if (file.length() > 1024 * 1024 * 10) {
                    file.delete()
                }
                try {
                    PrintStream(FileOutputStream(fileName, true)).use { printStream ->
                        val date = SimpleDateFormat.getDateTimeInstance().format(Date())
                        printStream.println(date + " [" + t.name + "]")
                        e.printStackTrace(printStream)
                    }
                } catch (ex: FileNotFoundException) {
                    ex.printStackTrace()
                }
            }
            Thread.sleep(5000)
            //重启应用
            val intent = packageManager.getLaunchIntentForPackage(packageName) ?: return
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            //退出程序
            Process.killProcess(Process.myPid())
        }
    }
}