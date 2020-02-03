package com.qianlei.jiaowu.core.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.qianlei.jiaowu.entity.Score;

import java.util.List;

/**
 * @author qianlei
 */
@Dao
public interface ScoreDao {
    /**
     * 添加新的分数信息
     *
     * @param scores 需要添加的分数
     */
    @Insert
    void insertScore(Score... scores);

    /**
     * 根据学年学期获取分数信息
     *
     * @param year 学年
     * @param term 学期
     * @return 该学生该学期的所有分数信息
     */
    @Query("SELECT * FROM score WHERE year=(:year) AND term = (:term)")
    List<Score> selectAllScoreByYearAndTerm(String year, String term);

    /**
     * 删除该学年学期的分数信息
     *
     * @param year 学年
     * @param term 学期
     */
    @Query("DELETE FROM score WHERE year=(:year) AND term = (:term)")
    void deleteAllScoreByYearAndTerm(String year, String term);

    /**
     * 删除所有分数信息
     */
    @Query("DELETE FROM score")
    void deleteAll();
}
