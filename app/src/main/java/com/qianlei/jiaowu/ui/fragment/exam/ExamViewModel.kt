package com.qianlei.jiaowu.ui.fragment.exam

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.qianlei.jiaowu.common.Result
import com.qianlei.jiaowu.common.ResultType
import com.qianlei.jiaowu.common.Term
import com.qianlei.jiaowu.repository.ExamRepository

/**
 * 考试信息相关的ViewModel
 *
 * @author qianlei
 */
class ExamViewModel(app: Application) : AndroidViewModel(app) {
    private val examRepository = ExamRepository(app)
    val examData = examRepository.examData

    /**
     * 刷新数据
     *
     * @param term 学期
     */
    fun refreshData(term: Term) {
        val task = ExamRepository.GetExamDataTask(examRepository)
        task.execute(term)
    }

    /**
     * 修改当前学期
     *
     * @param term 修改后的学期
     */
    fun changeTerm(term: Term) {
        examData.value = Result(ResultType.OK, "", ArrayList())
        val task = ExamRepository.GetExamDataUseCacheTask(examRepository)
        task.execute(term)
    }
}