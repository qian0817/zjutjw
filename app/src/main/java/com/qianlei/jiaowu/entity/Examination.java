package com.qianlei.jiaowu.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

/**
 * 考试信息的实体类
 *
 * @author qianlei
 */
@Data
@Entity
public class Examination {
    @PrimaryKey(autoGenerate = true)
    private int id;
    /**
     * 考试名称
     */
    @JSONField(name = "kcmc")
    @ColumnInfo(name = "name")
    private String name;
    /**
     * 考试地点
     */
    @JSONField(name = "cdmc")
    @ColumnInfo(name = "place")
    private String place;
    /**
     * 考试时间
     */
    @JSONField(name = "kssj")
    @ColumnInfo(name = "time")
    private String time;

    /**
     * 座位号
     */
    @JSONField(name = "zwh")
    @ColumnInfo(name = "seat_id")
    private String seatId;

    /**
     * 学年
     */
    @ColumnInfo(name = "year")
    private String year;

    /**
     * 学期
     */
    @ColumnInfo(name = "term")
    private String term;
}
