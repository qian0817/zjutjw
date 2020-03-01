package com.qianlei.jiaowu.ui.fragment.subject

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.qianlei.jiaowu.common.Result
import com.qianlei.jiaowu.common.ResultType
import com.qianlei.jiaowu.common.Term
import com.qianlei.jiaowu.repository.SubjectRepository

/**
 * @author qianlei
 */
class SubjectViewModel(app: Application) : AndroidViewModel(app) {
    private val subjectRepository = SubjectRepository(app)
    val subjectData = subjectRepository.subjectLiveData

    /**
     * 刷新数据
     *
     * @param term 学期
     */
    fun refreshData(term: Term) {
        val task = SubjectRepository.GetSubjectDataTask(subjectRepository)
        task.execute(term)
    }

    /**
     * 修改学期
     * @param term 修改后学期
     */
    fun changeTerm(term: Term) {
        subjectData.value = Result(ResultType.OK, "", ArrayList())
        val task = SubjectRepository.GetSubjectDataUseCache(subjectRepository)
        task.execute(term)
    }


}