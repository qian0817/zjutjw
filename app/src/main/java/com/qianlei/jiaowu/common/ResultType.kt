package com.qianlei.jiaowu.common

/**
 * 用于定义返回结果的类型
 *
 * @author qianlei
 */
enum class ResultType {
    /**
     * 成功
     */
    OK,
    /**
     * 输入的参数错误
     * 例如登陆时输入的学号或密码错误
     */
    PARAMS_ERROR,
    /**
     * 需要登录
     */
    NEED_LOGIN,
    /**
     * IO错误
     * 例如 网络问题
     */
    IO,
    /**
     * 其他错误
     */
    OTHER
}