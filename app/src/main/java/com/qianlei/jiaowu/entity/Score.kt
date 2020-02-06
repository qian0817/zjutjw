package com.qianlei.jiaowu.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alibaba.fastjson.annotation.JSONField

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
    @JSONField(name = "kcmc")
    var name: String? = null
    /**
     * 分数
     */
    @ColumnInfo(name = "score")
    @JSONField(name = "cj")
    var score: String? = null
    /**
     * 学年
     */
    @ColumnInfo(name = "year")
    @JSONField(name = "xnm")
    var year: String? = null
    /**
     * 学期
     */
    @ColumnInfo(name = "term")
    @JSONField(name = "xqm")
    var term: String? = null
    /**
     * 学分
     */
    @ColumnInfo(name = "credit")
    @JSONField(name = "xf")
    var credit: String? = null
    /**
     * 绩点
     */
    @ColumnInfo(name = "grade_point")
    @JSONField(name = "jd")
    var gradePoint: String? = null
}