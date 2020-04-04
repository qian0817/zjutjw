package com.qianlei.jiaowu.repository

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import com.qianlei.jiaowu.common.Result
import com.qianlei.jiaowu.common.ResultType
import com.qianlei.jiaowu.common.Term
import com.qianlei.jiaowu.db.MyDataBase
import com.qianlei.jiaowu.db.dao.ExamDao
import com.qianlei.jiaowu.entity.Examination
import com.qianlei.jiaowu.net.StudentClient

/**
 * 考试仓库类
 *
 * @author qianlei
 */
class ExamRepository constructor(private val context: Context) {
    val examData = MutableLiveData<Result<List<Examination>>>()
    private val examDao: ExamDao = MyDataBase.getDatabase(context).examDao()

    private fun getDataFromNet(term: Term): Result<List<Examination>> {
        val result = StudentClient.getStudentExamInformation(context, term.year, term.term)
        if (result.isSuccess()) {
            //向数据库中添加数据
            val examDao: ExamDao = MyDataBase.getDatabase(context).examDao()
            val examinationList = result.data
            examDao.deleteAllByYearAndTerm(term.year, term.term)
            if (examinationList != null) {
                for (examination in examinationList) {
                    examDao.insertExam(examination)
                }
            }
        }
        return result
    }

    private fun getDataFromDatabase(term: Term): Result<List<Examination>> {
        val examinationList = examDao.selectAllExamByYearAndTerm(term.year, term.term)
        return if (examinationList.isNotEmpty()) {
            Result(ResultType.OK, "从数据获取成功", examinationList)
        } else {
            Result(ResultType.OTHER, "数据库中无数据")
        }
    }

    class GetExamDataUseCacheTask constructor(private val examRepository: ExamRepository) : AsyncTask<Term, Void, Result<List<Examination>>?>() {
        override fun doInBackground(vararg params: Term?): Result<List<Examination>>? {
            if (params.size != 1) {
                return null
            }
            val term = params[0] ?: return null
            val result = examRepository.getDataFromDatabase(term)
            return if (result.isSuccess()) {
                result
            } else {
                examRepository.getDataFromNet(term)
            }
        }

        override fun onPostExecute(result: Result<List<Examination>>?) {
            if (result == null) {
                return
            }
            examRepository.examData.value = result
        }
    }

    class GetExamDataTask constructor(private val examRepository: ExamRepository) : AsyncTask<Term, Void, Result<List<Examination>>?>() {

        override fun doInBackground(vararg params: Term?): Result<List<Examination>>? {
            if (params.size != 1) {
                return null
            }
            val term = params[0] ?: return null
            return examRepository.getDataFromNet(term)
        }

        override fun onPostExecute(result: Result<List<Examination>>?) {
            if (result == null) {
                return
            }
            examRepository.examData.value = result
        }
    }

}