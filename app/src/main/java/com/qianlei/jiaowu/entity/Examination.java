package com.qianlei.jiaowu.entity;

import com.alibaba.fastjson.annotation.JSONField;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * 考试信息的实体类
 *
 * @author qianlei
 */
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

    public Examination() {
    }

    public Examination(String name, String place, String time) {
        this.name = name;
        this.place = place;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Examination that = (Examination) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(place, that.place) &&
                Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, place, time);
    }

    @NotNull
    @Override
    public String toString() {
        return "Examination{" +
                "name='" + name + '\'' +
                ", place='" + place + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
