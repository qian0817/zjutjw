package com.qianlei.jiaowu.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.qianlei.jiaowu.entity.Score

/**
 * @author qianlei
 */
@Dao
interface ScoreDao {
    /**
     * 添加新的分数信息
     *
     * @param scores 需要添加的分数
     */
    @Insert
    fun insertScore(vararg scores: Score?)

    /**
     * 根据学年学期获取分数信息
     *
     * @param year 学年
     * @param term 学期
     * @return 该学生该学期的所有分数信息
     */
    @Query("SELECT * FROM score WHERE year=(:year) AND term = (:term)")
    fun selectAllScoreByYearAndTerm(year: String?, term: String?): List<Score>

    /**
     * 删除该学年学期的分数信息
     *
     * @param year 学年
     * @param term 学期
     */
    @Query("DELETE FROM score WHERE year=(:year) AND term = (:term)")
    fun deleteAllScoreByYearAndTerm(year: String?, term: String?)

    /**
     * 删除所有分数信息
     */
    @Query("DELETE FROM score")
    fun deleteAll()
}