package com.qianlei.jiaowu.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

/**
 * 学生成绩的实体类
 *
 * @author qianlei
 */
@Data
@Entity
public class Score {
    @PrimaryKey(autoGenerate = true)
    public int id;
    /**
     * 考试名称
     */
    @ColumnInfo(name = "name")
    @JSONField(name = "kcmc")
    private String name;
    /**
     * 分数
     */
    @ColumnInfo(name = "score")
    @JSONField(name = "cj")
    private String score;
    /**
     * 学年
     */
    @ColumnInfo(name = "year")
    @JSONField(name = "xnm")
    private String year;
    /**
     * 学期
     */
    @ColumnInfo(name = "term")
    @JSONField(name = "xqm")
    private String term;
}
