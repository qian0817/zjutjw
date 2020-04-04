package com.qianlei.jiaowu.repository

import android.content.Context
import com.qianlei.jiaowu.common.Result
import com.qianlei.jiaowu.common.ResultType
import com.qianlei.jiaowu.common.Term
import com.qianlei.jiaowu.db.MyDataBase
import com.qianlei.jiaowu.entity.Score
import com.qianlei.jiaowu.net.StudentClient

/**
 * 课程的仓库类
 *
 * @author qianlei
 */
object ScoreRepository {


    /**
     * 从数据库中获取数据
     * @param term 需要获取的学期
     * @return 获取到的结果
     */
    private suspend fun getDataFromDatabase(context: Context, term: Term): Result<List<Score>> {
        val scoreDao = MyDataBase.getDatabase(context).scoreDao()
        val scoreList = scoreDao.selectAllScoreByYearAndTerm(term.year, term.term)
        return if (scoreList.isNotEmpty()) {
            Result(ResultType.OK, "从数据获取成功", scoreList)
        } else {
            Result(ResultType.OTHER, "数据库中无数据")
        }
    }

    /**
     * 从教务系统获取数据
     * @param term 学期
     * @return 获取到的数据
     */
    private suspend fun getDataFromNet(context: Context, term: Term): Result<List<Score>> {
        val result = StudentClient.getStudentScore(context, term.year, term.term)
        if (result.isSuccess()) {
            //向数据库中添加获取到的数据
            val scoreDao = MyDataBase.getDatabase(context).scoreDao()
            val scoreList = result.data
            scoreDao.deleteAllScoreByYearAndTerm(term.year, term.term)
            if (scoreList != null) {
                for (score in scoreList) {
                    scoreDao.insertScore(score)
                }
            }
        }
        return result
    }

    /**
     * 不使用缓存获取数据
     * @param term 学期
     */
    suspend fun getScoreDataNotUseCache(context: Context, term: Term): Result<List<Score>> {
        return getDataFromNet(context, term)
    }


    /**
     * 使用缓存获取数据
     * @param term 学期
     */
    suspend fun getScoreDataUseCache(context: Context, term: Term): Result<List<Score>> {
        val result = getDataFromDatabase(context, term)
        return if (result.isSuccess()) {
            result
        } else {
            getDataFromNet(context, term)
        }
    }
}