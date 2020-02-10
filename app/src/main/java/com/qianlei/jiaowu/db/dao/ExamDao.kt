package com.qianlei.jiaowu.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.qianlei.jiaowu.entity.Examination

/**
 * @author qianlei
 */
@Dao
interface ExamDao {
    /**
     * 添加考试信息
     *
     * @param examination 所有的考试信息
     */
    @Insert
    fun insertExam(vararg examination: Examination?)

    /**
     * 根据学年以及学期获取考试信息
     *
     * @param year 学年
     * @param term 学期
     * @return 该学期所有的考试信息
     */
    @Query("SELECT * FROM examination WHERE year = (:year) AND term = (:term)")
    fun selectAllExamByYearAndTerm(year: String?, term: String?): List<Examination>

    /**
     * 删除所有同年同学期的考试信息
     *
     * @param year 学年
     * @param term 学期
     */
    @Query("DELETE FROM examination WHERE year = (:year) AND term = (:term)")
    fun deleteAllByYearAndTerm(year: String?, term: String?)

    /**
     * 删除所有信息
     */
    @Query("DELETE FROM examination")
    fun deleteAll()
}