package com.qianlei.jiaowu

import android.app.Application
import android.os.Process
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.PrintStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess

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
            Process.killProcess(Process.myPid())
            exitProcess(1)
        }
    }


}