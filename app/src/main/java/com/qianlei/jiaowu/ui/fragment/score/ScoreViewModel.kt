package com.qianlei.jiaowu.ui.fragment.score

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.qianlei.jiaowu.common.Term
import com.qianlei.jiaowu.repository.ScoreRepository

/**
 * @author qianlei
 */
class ScoreViewModel(app: Application) : AndroidViewModel(app) {
    private val scoreRepository = ScoreRepository(app)
    val scoreData = scoreRepository.scoreData

    /**
     * 刷新数据
     *
     * @param term 学期
     */
    fun refreshData(term: Term) {
        val task = ScoreRepository.GetScoreDataNotUseCacheTask(scoreRepository)
        task.execute(term)
    }


    /**
     * 修改学期
     *
     * @param term 学期
     */
    fun changeTerm(term: Term) {
        val task = ScoreRepository.GetScoreDataUseCacheTask(scoreRepository)
        task.execute(term)
    }

}