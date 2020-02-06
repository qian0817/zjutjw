package com.qianlei.jiaowu.common


/**
 * 用于传递执行结果的类
 *
 * @author qianlei
 */
class Result<T> {
    /**
     * 是否成功以及相关的错误类型
     */
    var type: ResultType
    /**
     * 需要传递的消息
     */
    var msg: String
    /**
     * 传递的数据
     */
    var data: T? = null

    constructor(type: ResultType, msg: String) {
        this.type = type
        this.msg = msg
    }

    constructor(type: ResultType, msg: String, data: T) {
        this.type = type
        this.msg = msg
        this.data = data
    }

    fun isSuccess(): Boolean {
        return type == ResultType.OK
    }
}