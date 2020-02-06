package com.qianlei.jiaowu.utils

import java.util.*

/**
 * 日期工具类
 *
 * @author qianlei
 */
object DateUtil {
    /**
     * 获取当前日期对应的学年
     *
     * @return 当前日期对应的学年
     */
    fun getCurYear(): Int {
        val calendar = Calendar.getInstance()
        var year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        if (month < 9) {
            year--
        }
        return year
    }

    /**
     * 获取当前日期对应的学期
     *
     * @return 当前日期对应的学期
     */
    fun getCurTerm(): Int {
        val calendar = Calendar.getInstance()
        val month = calendar[Calendar.MONTH] + 1
        return if (month == 7 || month == 8) {
            3
        } else if (month in 2..6) {
            2
        } else {
            1
        }
    }
}