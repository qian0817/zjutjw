package com.qianlei.jiaowu.core.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.qianlei.jiaowu.entity.Subject;

import java.util.List;

/**
 * @author qianlei
 */
@Dao
public interface SubjectDao {
    /**
     * 添加新的课程信息
     *
     * @param subjects 需要添加的课程
     */
    @Insert
    void insertSubject(Subject... subjects);

    /**
     * 根据学年学期获取所有的课程信息
     *
     * @param year 学年
     * @param term 学期
     * @return 该学年学期所有的课程信息
     */
    @Query("SELECT * FROM subject WHERE year=(:year) AND term = (:term)")
    List<Subject> selectAllSubjectByYearAndTerm(String year, String term);

    /**
     * 根据学年学期获取所有的课程信息
     *
     * @param year 学年
     * @param term 学期
     */
    @Query("DELETE FROM subject WHERE year=(:year) AND term = (:term)")
    void deleteAllSubjectByYearAndTerm(String year, String term);

    /**
     * 删除所有课程信息
     */
    @Query("DELETE FROM subject")
    void deleteAll();
}
