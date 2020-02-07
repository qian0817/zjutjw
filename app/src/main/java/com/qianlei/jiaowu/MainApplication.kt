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
class MainApplication : Application() {
    companion object {
        private var application: MainApplication? = null

        fun getInstance(): MainApplication {
            return application!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler(CrashExceptionHandler())
        application = this
    }

    /**
     * 用于未处理异常处理
     */
    internal inner class CrashExceptionHandler : Thread.UncaughtExceptionHandler {
        override fun uncaughtException(t: Thread, e: Throwable) {
            //记录异常信息
            val cacheDir = externalCacheDir
            if (cacheDir != null) {
                val fileName = cacheDir.absolutePath + File.separator + "error.log"
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