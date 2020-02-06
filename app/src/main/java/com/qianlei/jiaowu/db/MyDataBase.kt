package com.qianlei.jiaowu.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.qianlei.jiaowu.db.dao.ExamDao
import com.qianlei.jiaowu.db.dao.ScoreDao
import com.qianlei.jiaowu.db.dao.SubjectDao
import com.qianlei.jiaowu.entity.Examination
import com.qianlei.jiaowu.entity.Score
import com.qianlei.jiaowu.entity.Subject

/**
 * @author qianlei
 */
@Database(entities = [Examination::class, Score::class, Subject::class], version = 1, exportSchema = false)
abstract class MyDataBase : RoomDatabase() {
    abstract fun examDao(): ExamDao
    abstract fun scoreDao(): ScoreDao
    abstract fun subjectDao(): SubjectDao

    companion object {
        private var INSTANCE: MyDataBase? = null
        fun getDatabase(context: Context?): MyDataBase {
            if (INSTANCE == null) {
                synchronized(MyDataBase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context!!.applicationContext,
                                MyDataBase::class.java, "database")
                                .build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}