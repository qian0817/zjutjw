package com.qianlei.jiaowu.ui.fragment.score

import android.app.Application
import android.os.Handler
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.qianlei.jiaowu.common.Result
import com.qianlei.jiaowu.common.ResultType
import com.qianlei.jiaowu.db.MyDataBase
import com.qianlei.jiaowu.db.dao.ScoreDao
import com.qianlei.jiaowu.entity.Score
import com.qianlei.jiaowu.net.StudentApi
import java.util.concurrent.*

/**
 * @author qianlei
 */
class ScoreViewModel(private val app: Application) : AndroidViewModel(app) {
    private val executor: ExecutorService = ThreadPoolExecutor(1, 1,
            0, TimeUnit.SECONDS, SynchronousQueue(), ThreadFactory { r: Runnable? -> Thread(r, "获取成绩线程") },
            ThreadPoolExecutor.DiscardPolicy())
    private val handler = Handler()
    val result = MutableLiveData<Result<List<Score>>>()
    private val studentApi: StudentApi = StudentApi.getStudentApi(app)
    private var scoreDao: ScoreDao
    private var getFromDatabase = true

    /**
     * 刷新数据
     *
     * @param year 学年
     * @param term 学期
     */
    fun refreshData(year: String, term: String) {
        executor.execute {
            if (getFromDatabase) {
                getFromDatabase = false
                val score = getDataFromDatabase(year, term)
                //如果数据库中有数据则修改数据，否则从网络中获取
                if (score.isSuccess()) {
                    handler.post { result.setValue(score) }
                    return@execute
                }
            }
            val score = getDataFromNet(year, term)
            handler.post { result.setValue(score) }
        }
    }

    /**
     * 修改学期
     *
     * @param year 学年
     * @param term 学期
     */
    fun changeTerm(year: String, term: String) { //第一次先从数据中获取数据
        getFromDatabase = true
        refreshData(year, term)
    }

    private fun getDataFromDatabase(year: String, term: String): Result<List<Score>> {
        scoreDao = MyDataBase.getDatabase(app).scoreDao()
        val scoreList = scoreDao.selectAllScoreByYearAndTerm(year, term)
        return if (scoreList.isNotEmpty()) {
            Result(ResultType.OK, "从数据获取成功", scoreList)
        } else {
            Result(ResultType.OTHER, "数据库中无数据")
        }
    }

    private fun getDataFromNet(year: String, term: String): Result<List<Score>> {
        val result = studentApi.getStudentScore(year, term)
        if (result.isSuccess()) { //向数据库中添加数据
            scoreDao = MyDataBase.getDatabase(app).scoreDao()
            val scoreList = result.data
            scoreDao.deleteAllScoreByYearAndTerm(year, term)
            if (scoreList != null) {
                for (score in scoreList) {
                    scoreDao.insertScore(score)
                }
            }
        }
        return result
    }

    init {
        scoreDao = MyDataBase.getDatabase(app).scoreDao()
    }
}