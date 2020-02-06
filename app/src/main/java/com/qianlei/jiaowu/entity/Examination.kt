package com.qianlei.jiaowu.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alibaba.fastjson.annotation.JSONField

/**
 * 考试信息的实体类
 *
 * @author qianlei
 */
@Entity
class Examination {

    @PrimaryKey(autoGenerate = true)
    var id = 0
    /**
     * 考试名称
     */
    @JSONField(name = "kcmc")
    @ColumnInfo(name = "name")
    var name: String? = null
    /**
     * 考试地点
     */
    @JSONField(name = "cdmc")
    @ColumnInfo(name = "place")
    var place: String? = null
    /**
     * 考试时间
     */
    @JSONField(name = "kssj")
    @ColumnInfo(name = "time")
    var time: String? = null
    /**
     * 座位号
     */
    @JSONField(name = "zwh")
    @ColumnInfo(name = "seat_id")
    var seatId: String? = null
    /**
     * 学年
     */
    @ColumnInfo(name = "year")
    var year: String? = null
    /**
     * 学期
     */
    @ColumnInfo(name = "term")
    var term: String? = null
}