package com.qianlei.jiaowu.core.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.qianlei.jiaowu.core.db.dao.ExamDao;
import com.qianlei.jiaowu.core.db.dao.ScoreDao;
import com.qianlei.jiaowu.core.db.dao.SubjectDao;
import com.qianlei.jiaowu.entity.Examination;
import com.qianlei.jiaowu.entity.Score;
import com.qianlei.jiaowu.entity.Subject;

/**
 * @author qianlei
 */
@Database(entities = {Examination.class, Score.class, Subject.class}, version = 1, exportSchema = false)
public abstract class MyDataBase extends RoomDatabase {
    private static MyDataBase INSTANCE;

    public static synchronized MyDataBase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (MyDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MyDataBase.class, "database").build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract ExamDao getExamDao();

    public abstract ScoreDao getScoreDao();

    public abstract SubjectDao getSubjectDao();
}
