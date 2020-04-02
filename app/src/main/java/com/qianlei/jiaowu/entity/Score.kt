package com.qianlei.jiaowu.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * 学生成绩的实体类
 *
 * @author qianlei
 */
@Entity
class Score {
    @PrimaryKey(autoGenerate = true)
    var id = 0
    /**
     * 考试名称
     */
    @ColumnInfo(name = "name")
    @SerializedName(value = "kcmc")
    var name: String? = null
    /**
     * 分数
     */
    @ColumnInfo(name = "score")
    @SerializedName(value = "cj")
    var score: String? = null
    /**
     * 学年
     */
    @ColumnInfo(name = "year")
    @SerializedName(value = "xnm")
    var year: String? = null
    /**
     * 学期
     */
    @ColumnInfo(name = "term")
    @SerializedName(value = "xqm")
    var term: String? = null
    /**
     * 学分
     */
    @ColumnInfo(name = "credit")
    @SerializedName(value = "xf")
    var credit: String? = null
    /**
     * 绩点
     */
    @ColumnInfo(name = "grade_point")
    @SerializedName(value = "jd")
    var gradePoint: String? = null
}