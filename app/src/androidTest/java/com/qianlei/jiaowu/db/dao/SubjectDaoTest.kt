package com.qianlei.jiaowu.db.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.qianlei.jiaowu.db.MyDataBase
import com.qianlei.jiaowu.entity.Subject
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SubjectDaoTest {
    private lateinit var subjectDao: SubjectDao
    private lateinit var dataBase: MyDataBase
    @Before
    fun start() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dataBase = Room.inMemoryDatabaseBuilder(context, MyDataBase::class.java).build()
        subjectDao = dataBase.subjectDao()
    }

    @After
    fun end() {
        dataBase.close()
    }

    @Test
    suspend fun testSubjectDao() {
        //测试InsertScore
        val subject = Subject()
        subject.teacher = "测试教师"
        subject.place = "测试地址"
        subject.name = "测试课程"
        subject.week = "1-8周"
        subject.day = "星期一"
        subject.last = "2-4节"
        subject.year = "2019"
        subject.term = "3"
        subjectDao.insertSubject(subject)
        subject.term = "12"
        subjectDao.insertSubject(subject)
        //测试deleteAllScoreByYearAndTerm
        subjectDao.deleteAllSubjectByYearAndTerm("2019", "3")
        var list = subjectDao.selectAllSubjectByYearAndTerm("2019", "3")
        Assert.assertEquals(list.size, 0)
        list = subjectDao.selectAllSubjectByYearAndTerm("2019", "12")
        Assert.assertEquals(list.size, 1)
        //测试deleteAll
        subjectDao.deleteAll()
        list = subjectDao.selectAllSubjectByYearAndTerm("2019", "12")
        Assert.assertEquals(list.size, 0)
    }
}