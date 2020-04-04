package com.qianlei.jiaowu.db.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.qianlei.jiaowu.db.MyDataBase
import com.qianlei.jiaowu.entity.Examination
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExamDaoTest {
    private lateinit var examDao: ExamDao
    private lateinit var dataBase: MyDataBase

    @Before
    fun start() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dataBase = Room.inMemoryDatabaseBuilder(context, MyDataBase::class.java).build()
        examDao = dataBase.examDao()
    }

    @After
    fun end() {
        dataBase.close()
    }

    @Test
    suspend fun test() {
        //测试insertExam
        val examination = Examination()
        examination.name = "测试考试"
        examination.place = "测试地址"
        examination.seatId = "2"
        examination.year = "2019"
        examination.term = "3"
        examination.time = "2019-1-1 00:00-01:00"
        examDao.insertExam(examination)
        examination.term = "12"
        examDao.insertExam(examination)
        //测试deleteAllByYearAndTerm
        examDao.deleteAllByYearAndTerm("2019", "3")
        var list: List<Examination?> = examDao.selectAllExamByYearAndTerm("2019", "3")
        Assert.assertEquals(list.size.toLong(), 0)
        list = examDao.selectAllExamByYearAndTerm("2019", "12")
        Assert.assertEquals(list.size.toLong(), 1)
        //测试deleteAll
        examDao.deleteAll()
        list = examDao.selectAllExamByYearAndTerm("2019", "12")
        Assert.assertEquals(list.size.toLong(), 0)
    }
}