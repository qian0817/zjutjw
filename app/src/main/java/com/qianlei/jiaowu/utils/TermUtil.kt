package com.qianlei.jiaowu.utils

import com.qianlei.jiaowu.common.Term
import java.util.*

object TermUtil {
    fun getNowTerm(): Term {
        val calendar = Calendar.getInstance()
        var year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        if (month < 9) {
            year--
        }
        val term = if (month == 7 || month == 8) {
            3
        } else if (month in 2..6) {
            2
        } else {
            1
        }
        return Term(year.toString(), term.toString())
    }
}