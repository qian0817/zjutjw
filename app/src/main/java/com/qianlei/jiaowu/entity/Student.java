package com.qianlei.jiaowu.entity;

import com.alibaba.fastjson.annotation.JSONField;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * 学生信息的实体类
 *
 * @author qianlei
 */
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

    public Student() {
    }

    public Student(String studentId, String sex, String college, String grade, String status) {
        this.studentId = studentId;
        this.sex = sex;
        this.college = college;
        this.grade = grade;
        this.status = status;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Student student = (Student) o;
        return Objects.equals(studentId, student.studentId) &&
                Objects.equals(sex, student.sex) &&
                Objects.equals(college, student.college) &&
                Objects.equals(grade, student.grade) &&
                Objects.equals(status, student.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, sex, college, grade, status);
    }

    @NotNull
    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", sex='" + sex + '\'' +
                ", college='" + college + '\'' +
                ", grade='" + grade + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
