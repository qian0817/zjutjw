package com.qianlei.jiaowu.entity;

import com.alibaba.fastjson.annotation.JSONField;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * 学生成绩的实体类
 *
 * @author qianlei
 */
public class Score {
    @JSONField(name = "kcmc")
    private String name;
    @JSONField(name = "cj")
    private String score;

    public Score() {
    }

    public Score(String name, String score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Score score1 = (Score) o;
        return Objects.equals(name, score1.name) &&
                Objects.equals(score, score1.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, score);
    }

    @NotNull
    @Override
    public String toString() {
        return "Score{" +
                "name='" + name + '\'' +
                ", score='" + score + '\'' +
                '}';
    }
}
