package com.qianlei.jiaowu.repository

import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

object SettingRepository {
    /**
     * 设置学期开始时间
     * @param year 年
     * @param month 月
     * @param day 日
     */
    fun setStartDay(c: Context, year: Int, month: Int, day: Int) {
        val sharedPreferences = c.getSharedPreferences("setting", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("year", year)
        editor.putInt("month", month)
        editor.putInt("day", day)
        editor.apply()
    }

    /**
     * 获取学期开始时间
     * @return
     */
    fun getStartDay(c: Context): Date {
        val sharedPreferences = c.getSharedPreferences("setting", Context.MODE_PRIVATE)
        val year = sharedPreferences.getInt("year", 2020)
        val month = sharedPreferences.getInt("month", 1)
        val day = sharedPreferences.getInt("day", 16)
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        return calendar.time
    }

    /**
     * 获取格式化后的学期开始时间
     * 格式为yyyy-MM-dd
     * @return 格式化后的学期开始时间
     */
    fun getStartDayFormat(c: Context): String {
        val date = getStartDay(c)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        return dateFormat.format(date)
    }

}