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
    @Insert
    void insertSubject(Score... scores);

    @Query("SELECT * FROM score WHERE year=(:year) AND term = (:term)")
    List<Score> selectAllSubjectByYearAndTerm(String year, String term);

    @Query("DELETE FROM score WHERE year=(:year) AND term = (:term)")
    void deleteAllSubjectByYearAndTerm(String year, String term);
}
