package com.qianlei.jiaowu.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * 用于传递执行结果的类
 *
 * @author qianlei
 */
@Getter
@EqualsAndHashCode
@ToString
public class Result<T> {
    /**
     * 是否成功以及相关的错误类型
     */
    private ResultType type;
    /**
     * 需要传递的消息
     */
    private String msg;
    /**
     * 传递的数据
     */
    private T data;

    public Result(ResultType type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public Result(ResultType type, String msg, T data) {
        this.type = type;
        this.msg = msg;
        this.data = data;
    }

    public boolean isSuccess() {
        return type == ResultType.OK;
    }
}
