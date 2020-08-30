package com.qianlei.jiaowu.ui.fragment.exam

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.qianlei.jiaowu.common.Result
import com.qianlei.jiaowu.common.ResultType
import com.qianlei.jiaowu.entity.Examination
import com.qianlei.jiaowu.entity.Term
import com.qianlei.jiaowu.repository.ExamRepository
import kotlinx.coroutines.launch

/**
 * 考试信息相关的ViewModel
 *
 * @author qianlei
 */
class ExamViewModel(app: Application) : AndroidViewModel(app) {
    val examData = MutableLiveData<Result<List<Examination>>>()

    /**
     * 刷新数据
     *
     * @param term 学期
     */
    fun refreshData(term: Term) {
        viewModelScope.launch {
            examData.value = ExamRepository.getExamDataNotUseCache(getApplication(), term)
        }
    }

    /**
     * 修改当前学期
     *
     * @param term 修改后的学期
     */
    fun changeTerm(term: Term) {
        examData.value = Result(ResultType.OK, "", ArrayList())
        viewModelScope.launch {
            examData.value = ExamRepository.getExamDataUseCache(getApplication(), term)
        }
    }
}