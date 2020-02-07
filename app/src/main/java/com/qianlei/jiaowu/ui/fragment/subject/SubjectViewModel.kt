package com.qianlei.jiaowu.ui.fragment.subject

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.qianlei.jiaowu.common.Term
import com.qianlei.jiaowu.repository.SubjectRepository

/**
 * @author qianlei
 */
class SubjectViewModel(app: Application) : AndroidViewModel(app) {
    private val subjectRepository = SubjectRepository
    val subjectData = subjectRepository.subjectLiveData

    /**
     * 刷新数据
     *
     * @param term 学期
     */
    fun refreshData(term: Term) {
        val task = SubjectRepository.GetSubjectDataTask()
        task.execute(term)
    }

    /**
     * 修改学期
     * @param term 修改后学期
     */
    fun changeTerm(term: Term) {
        val task = SubjectRepository.GetSubjectDataUseCache()
        task.execute(term)
    }


}