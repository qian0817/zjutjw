package com.qianlei.jiaowu.repository

import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import com.qianlei.jiaowu.MainApplication
import com.qianlei.jiaowu.common.Result
import com.qianlei.jiaowu.common.ResultType
import com.qianlei.jiaowu.common.Term
import com.qianlei.jiaowu.db.MyDataBase
import com.qianlei.jiaowu.entity.Score
import com.qianlei.jiaowu.net.StudentApi

/**
 * 课程的仓库类
 *
 * @author qianlei
 */
class ScoreRepository {
    private var context = MainApplication.getInstance()
    private var scoreDao = MyDataBase.getDatabase(MainApplication.getInstance()).scoreDao()
    private val studentApi: StudentApi = StudentApi.getStudentApi(context)
    val scoreData = MutableLiveData<Result<List<Score>>>()

    /**
     * 从数据库中获取数据
     * @param term 需要获取的学期
     * @return 获取到的结果
     */
    private fun getDataFromDatabase(term: Term): Result<List<Score>> {
        scoreDao = MyDataBase.getDatabase(context).scoreDao()
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
    private fun getDataFromNet(term: Term): Result<List<Score>> {
        val result = studentApi.getStudentScore(term.year, term.term)
        if (result.isSuccess()) {
            //向数据库中添加获取到的数据
            scoreDao = MyDataBase.getDatabase(context).scoreDao()
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
     *  不使用缓存中直接从教务系统获取数据
     */
    class GetScoreDataNotUseCacheTask constructor(private val scoreRepository: ScoreRepository) : AsyncTask<Term, Void, Result<List<Score>>?>() {
        override fun doInBackground(vararg params: Term?): Result<List<Score>>? {
            if (params.size != 1) {
                return null
            }
            val term = params[0]
            return if (term != null) {
                scoreRepository.getDataFromNet(term)
            } else {
                null
            }
        }

        override fun onPostExecute(result: Result<List<Score>>?) {
            super.onPostExecute(result)
            scoreRepository.scoreData.value = result
        }
    }

    /**
     * 从缓存中获取数据
     * 如果缓存中无内容 那么从数据库中获取数据
     */
    class GetScoreDataUseCacheTask constructor(private val scoreRepository: ScoreRepository) : AsyncTask<Term, Void, Result<List<Score>>?>() {
        override fun doInBackground(vararg params: Term?): Result<List<Score>>? {
            if (params.size != 1) {
                return null
            }
            val term = params[0] ?: return null
            val result = scoreRepository.getDataFromDatabase(term)
            return if (result.isSuccess()) {
                result
            } else {
                scoreRepository.getDataFromNet(term)
            }
        }

        override fun onPostExecute(result: Result<List<Score>>?) {
            super.onPostExecute(result)
            scoreRepository.scoreData.value = result
        }
    }

}