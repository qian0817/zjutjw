package com.qianlei.jiaowu.ui.fragment.subject

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.qianlei.jiaowu.common.Result
import com.qianlei.jiaowu.common.ResultType
import com.qianlei.jiaowu.entity.Term
import com.qianlei.jiaowu.entity.Subject
import com.qianlei.jiaowu.repository.SubjectRepository
import kotlinx.coroutines.launch

/**
 * @author qianlei
 */
class SubjectViewModel(app: Application) : AndroidViewModel(app) {
    val subjectData = MutableLiveData<Result<List<Subject>>>()

    /**
     * 刷新数据
     *
     * @param term 学期
     */
    fun refreshData(term: Term) {
        viewModelScope.launch {
            subjectData.value = SubjectRepository.getSubjectDataNotUseCache(getApplication(), term)
        }
    }

    /**
     * 修改学期
     * @param term 修改后学期
     */
    fun changeTerm(term: Term) {
        subjectData.value = Result(ResultType.OK, "", ArrayList())
        viewModelScope.launch {
            subjectData.value = SubjectRepository.getSubjectDataUseCache(getApplication(), term)
        }
    }


}