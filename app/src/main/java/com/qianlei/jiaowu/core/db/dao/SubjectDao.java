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
    @Insert
    void insertSubject(Subject... subjects);

    @Query("SELECT * FROM subject WHERE year=(:year) AND term = (:term)")
    List<Subject> selectAllSubjectByYearAndTerm(String year, String term);

    @Query("DELETE FROM subject WHERE year=(:year) AND term = (:term)")
    void deleteAllSubjectByYearAndTerm(String year, String term);
}
