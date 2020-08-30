package com.qianlei.jiaowu.ui.fragment.score

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.qianlei.jiaowu.common.Result
import com.qianlei.jiaowu.common.ResultType
import com.qianlei.jiaowu.entity.Score
import com.qianlei.jiaowu.entity.Term
import com.qianlei.jiaowu.repository.ScoreRepository
import kotlinx.coroutines.launch

/**
 * @author qianlei
 */
class ScoreViewModel(app: Application) : AndroidViewModel(app) {
    val scoreData = MutableLiveData<Result<List<Score>>>()

    /**
     * 刷新数据
     *
     * @param term 学期
     */
    fun refreshData(term: Term) {
        viewModelScope.launch {
            scoreData.value = ScoreRepository.getScoreDataNotUseCache(getApplication(), term)
        }
    }


    /**
     * 修改学期
     *
     * @param term 学期
     */
    fun changeTerm(term: Term) {
        scoreData.value = Result(ResultType.OK, "", ArrayList())
        viewModelScope.launch {
            scoreData.value = ScoreRepository.getScoreDataUseCache(getApplication(), term)
        }
    }

}