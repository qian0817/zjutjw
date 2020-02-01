package com.qianlei.jiaowu.entity;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

/**
 * 学生信息的实体类
 *
 * @author qianlei
 */
@Data
public class Student {
    @JSONField(name = "xh_id")
    private String studentId;
    @JSONField(name = "xbm")
    private String sex;
    @JSONField(name = "jg_id")
    private String college;
    @JSONField(name = "bh_id")
    private String grade;
    @JSONField(name = "xjztdm")
    private String status;
}
