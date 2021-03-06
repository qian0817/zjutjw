package com.qianlei.jiaowu.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * 考试信息的实体类
 *
 * @author qianlei
 */
@Entity
data class Examination(

        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,
        /**
         * 考试名称
         */
        @SerializedName(value = "kcmc")
        @ColumnInfo(name = "name")
        var name: String? = null,
        /**
         * 考试地点
         */
        @SerializedName(value = "cdmc")
        @ColumnInfo(name = "place")
        var place: String? = null,
        /**
         * 考试时间
         */
        @SerializedName(value = "kssj")
        @ColumnInfo(name = "time")
        var time: String? = null,
        /**
         * 学年
         */
        @ColumnInfo(name = "year")
        var year: String? = null,
        /**
         * 学期
         */
        @ColumnInfo(name = "term")
        var term: String? = null
)