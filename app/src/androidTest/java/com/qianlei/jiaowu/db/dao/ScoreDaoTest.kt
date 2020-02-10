package com.qianlei.jiaowu.db.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.qianlei.jiaowu.db.MyDataBase
import com.qianlei.jiaowu.entity.Score
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScoreDaoTest {
    private lateinit var scoreDao: ScoreDao
    private lateinit var dataBase: MyDataBase
    @Before
    fun start() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dataBase = Room.inMemoryDatabaseBuilder(context, MyDataBase::class.java).build()
        scoreDao = dataBase.scoreDao()
    }

    @After
    fun end() {
        dataBase.close()
    }

    @Test
    fun testScoreDao() {
        //测试InsertScore
        val score = Score()
        score.credit = "1.0"
        score.gradePoint = "1.0"
        score.name = "测试成绩"
        score.score = "99"
        score.term = "3"
        score.year = "2019"
        scoreDao.insertScore(score)
        score.term = "12"
        scoreDao.insertScore(score)
        //测试deleteAllScoreByYearAndTerm
        scoreDao.deleteAllScoreByYearAndTerm("2019", "3")
        var list = scoreDao.selectAllScoreByYearAndTerm("2019", "3")
        Assert.assertEquals(list.size, 0)
        list = scoreDao.selectAllScoreByYearAndTerm("2019", "12")
        Assert.assertEquals(list.size, 1)
        //测试deleteAll
        scoreDao.deleteAll()
        list = scoreDao.selectAllScoreByYearAndTerm("2019", "12")
        Assert.assertEquals(list.size, 0)
    }
}