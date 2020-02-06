package com.qianlei.jiaowu.entity

import com.alibaba.fastjson.annotation.JSONField

/**
 * 学生信息的实体类
 *
 * @author qianlei
 */
class Student {
    @JSONField(name = "xh_id")
    var studentId: String? = null
    @JSONField(name = "xm")
    var name: String? = null
}