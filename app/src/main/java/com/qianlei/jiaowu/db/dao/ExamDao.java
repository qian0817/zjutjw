package com.qianlei.jiaowu.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.qianlei.jiaowu.entity.Examination;

import java.util.List;

/**
 * @author qianlei
 */
@Dao
public interface ExamDao {

    /**
     * 添加考试信息
     *
     * @param examination 所有的考试信息
     */
    @Insert
    void insertExam(Examination... examination);

    /**
     * 根据学年以及学期获取考试信息
     *
     * @param year 学年
     * @param term 学期
     * @return 该学期所有的考试信息
     */
    @Query("SELECT * FROM examination WHERE year = (:year) AND term = (:term)")
    List<Examination> selectAllExamByYearAndTerm(String year, String term);

    /**
     * 删除所有同年同学期的考试信息
     *
     * @param year 学年
     * @param term 学期
     */
    @Query("DELETE FROM examination WHERE year = (:year) AND term = (:term)")
    void deleteAllByYearAndTerm(String year, String term);

    /**
     * 删除所有信息
     */
    @Query("DELETE FROM examination")
    void deleteAll();
}
