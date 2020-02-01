package com.qianlei.jiaowu.entity;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

/**
 * 考试信息的实体类
 *
 * @author qianlei
 */
@Data
public class Examination {
    /**
     * 考试名称
     */
    @JSONField(name = "kcmc")
    private String name;
    /**
     * 考试地点
     */
    @JSONField(name = "cdmc")
    private String place;
    /**
     * 考试时间
     */
    @JSONField(name = "kssj")
    private String time;

    /**
     * 座位号
     */
    @JSONField(name = "zwh")
    private String seatId;
}
