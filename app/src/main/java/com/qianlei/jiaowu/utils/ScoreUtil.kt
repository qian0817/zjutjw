package com.qianlei.jiaowu.utils

import com.qianlei.jiaowu.entity.Score
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * @author qianlei
 */
object ScoreUtil {
    fun getGpa(scoreList: List<Score>?): String {
        if (scoreList == null || scoreList.isEmpty()) {
            return "暂无成绩信息"
        }
        var ans = BigDecimal("0")
        var all = BigDecimal("0")
        for (score in scoreList) {
            if (score.gradePoint != null && score.credit != null) {
                ans = ans.add(BigDecimal(score.credit).multiply(BigDecimal(score.gradePoint)))
                all = all.add(BigDecimal(score.credit))
            }
        }
        return if (all == BigDecimal.ZERO) {
            "暂无绩点信息"
        } else {
            ans.setScale(3, RoundingMode.HALF_UP).divide(all, RoundingMode.HALF_UP).toString()
        }
    }
}