package com.qianlei.jiaowu.utils

import com.qianlei.jiaowu.entity.Score
import org.junit.Assert
import org.junit.Test

class ScoreUtilTest {

    @Test
    fun getGpa() {
        Assert.assertEquals("暂无成绩信息", ScoreUtil.getGpa(null))
        Assert.assertEquals("暂无成绩信息", ScoreUtil.getGpa(ArrayList()))
        Assert.assertEquals("暂无绩点信息", ScoreUtil.getGpa(listOf(Score())))
        val score0 = Score()
        score0.gradePoint = "5"
        Assert.assertEquals("暂无绩点信息", ScoreUtil.getGpa(listOf(score0)))
        val score1 = Score()
        score1.gradePoint = "5"
        score1.credit = "1"
        Assert.assertEquals("5.000", ScoreUtil.getGpa(listOf(score1)))
        val score2 = Score()
        score2.gradePoint = "1"
        score2.credit = "1"
        Assert.assertEquals("3.000", ScoreUtil.getGpa(listOf(score1, score2)))
        val score3 = Score()
        score3.gradePoint = "2"
        score3.credit = "2"

        Assert.assertEquals("2.500", ScoreUtil.getGpa(listOf(score1, score2, score3)))
    }
}