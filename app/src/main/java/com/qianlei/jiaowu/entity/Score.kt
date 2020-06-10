package com.qianlei.jiaowu.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * 学生成绩的实体类
 *
 * @author qianlei
 */
@Entity
data class Score(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,
        /**
         * 考试名称
         */
        @ColumnInfo(name = "name")
        @SerializedName(value = "kcmc")
        var name: String? = null,
        /**
         * 分数
         */
        @ColumnInfo(name = "score")
        @SerializedName(value = "cj")
        var score: String? = null,
        /**
         * 学年
         */
        @ColumnInfo(name = "year")
        @SerializedName(value = "xnm")
        var year: String? = null,
        /**
         * 学期
         */
        @ColumnInfo(name = "term")
        @SerializedName(value = "xqm")
        var term: String? = null,
        /**
         * 学分
         */
        @ColumnInfo(name = "credit")
        @SerializedName(value = "xf")
        var credit: String? = null,
        /**
         * 绩点
         */
        @ColumnInfo(name = "grade_point")
        @SerializedName(value = "jd")
        var gradePoint: String? = null
) {
    companion object {
        @JvmStatic
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
}