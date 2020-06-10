package com.qianlei.jiaowu.entity

import java.util.*

/**
 * 学期实体类
 * @author qianlei
 */
data class Term(var year: String, var term: String) {
    companion object {
        @JvmStatic
        fun getNowTerm(): Term {
            val calendar = Calendar.getInstance()
            var year = calendar[Calendar.YEAR]
            val month = calendar[Calendar.MONTH] + 1
            if (month < 9) {
                year--
            }
            val term = if (month == 7 || month == 8) {
                "3"
            } else if (month in 2..6) {
                "2"
            } else {
                "1"
            }
            return Term(year.toString(), term)
        }
    }
}