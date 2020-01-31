package com.qianlei.jiaowu.common;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * 用于传递执行结果的类
 *
 * @author qianlei
 */
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

    public ResultType getType() {
        return type;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public boolean isSuccess() {
        return type == ResultType.OK;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Result<?> result = (Result<?>) o;
        return type == result.type &&
                Objects.equals(msg, result.msg) &&
                Objects.equals(data, result.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, msg, data);
    }

    @Override
    @NotNull
    public String toString() {
        return "Result{" +
                "type=" + type +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
