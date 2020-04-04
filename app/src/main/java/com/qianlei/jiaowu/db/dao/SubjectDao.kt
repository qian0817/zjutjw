package com.qianlei.jiaowu.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.qianlei.jiaowu.entity.Subject

/**
 * @author qianlei
 */
@Dao
interface SubjectDao {
    /**
     * 添加新的课程信息
     *
     * @param subjects 需要添加的课程
     */
    @Insert
    suspend fun insertSubject(vararg subjects: Subject?)

    /**
     * 根据学年学期获取所有的课程信息
     *
     * @param year 学年
     * @param term 学期
     * @return 该学年学期所有的课程信息
     */
    @Query("SELECT * FROM subject WHERE year=(:year) AND term = (:term)")
    suspend fun selectAllSubjectByYearAndTerm(year: String?, term: String?): List<Subject>

    /**
     * 根据学年学期获取所有的课程信息
     *
     * @param year 学年
     * @param term 学期
     */
    @Query("DELETE FROM subject WHERE year=(:year) AND term = (:term)")
    suspend fun deleteAllSubjectByYearAndTerm(year: String?, term: String?)

    /**
     * 删除所有课程信息
     */
    @Query("DELETE FROM subject")
    suspend fun deleteAll()
}