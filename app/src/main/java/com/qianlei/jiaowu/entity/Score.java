package com.qianlei.jiaowu.entity;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

/**
 * 学生成绩的实体类
 *
 * @author qianlei
 */
@Data
public class Score {
    @JSONField(name = "kcmc")
    private String name;
    @JSONField(name = "cj")
    private String score;
}
