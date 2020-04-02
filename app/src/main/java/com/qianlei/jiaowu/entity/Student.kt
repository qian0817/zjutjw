package com.qianlei.jiaowu.entity

import com.google.gson.annotations.SerializedName

/**
 * 学生信息的实体类
 *
 * @author qianlei
 */
class Student {
    @SerializedName(value = "xh_id")
    var studentId: String? = null

    @SerializedName(value = "xm")
    var name: String? = null
}