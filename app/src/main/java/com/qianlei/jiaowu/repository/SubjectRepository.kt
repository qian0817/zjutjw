package com.qianlei.jiaowu.repository

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import com.qianlei.jiaowu.common.Result
import com.qianlei.jiaowu.common.ResultType
import com.qianlei.jiaowu.common.Term
import com.qianlei.jiaowu.db.MyDataBase
import com.qianlei.jiaowu.db.dao.SubjectDao
import com.qianlei.jiaowu.entity.Subject
import com.qianlei.jiaowu.net.StudentApi

/**
 * 课程的仓库类
 *
 * @author qianlei
 */
class SubjectRepository constructor(context: Context) {
    private var subjectDao: SubjectDao = MyDataBase.getDatabase(context).subjectDao()
    private val studentApi: StudentApi = StudentApi.getStudentApi(context)
    val subjectLiveData = MutableLiveData<Result<List<Subject>>>()

    private fun getDataFromDatabase(term: Term): Result<List<Subject>> {
        val subjectList = subjectDao.selectAllSubjectByYearAndTerm(term.year, term.term)
        return if (subjectList.isNotEmpty()) {
            Result(ResultType.OK, "从数据获取成功", subjectList)
        } else {
            Result(ResultType.OTHER, "数据库中无数据")
        }
    }

    private fun getDataFromNet(term: Term): Result<List<Subject>> {
        val result = studentApi.getStudentTimetable(term.year, term.term)
        if (result.isSuccess()) {
            //向数据库中添加数据
            val subjectList = result.data
            if (subjectList != null) {
                subjectDao.deleteAllSubjectByYearAndTerm(term.year, term.term)
                for (subject in subjectList) {
                    subjectDao.insertSubject(subject)
                }
            }
        }
        return result
    }

    class GetSubjectDataUseCache constructor(private val subjectRepository: SubjectRepository) : AsyncTask<Term, Void, Result<List<Subject>>?>() {
        override fun doInBackground(vararg params: Term?): Result<List<Subject>>? {
            if (params.size != 1) {
                return null
            }
            val term = params[0] ?: return null
            //先从数据库中判断是否存在数据 不存在则继续从网络中获取数据
            val result = subjectRepository.getDataFromDatabase(term)
            return if (result.isSuccess()) {
                result
            } else {
                subjectRepository.getDataFromNet(term)
            }
        }

        override fun onPostExecute(result: Result<List<Subject>>?) {
            super.onPostExecute(result)
            subjectRepository.subjectLiveData.value = result
        }
    }

    class GetSubjectDataTask constructor(private val subjectRepository: SubjectRepository) : AsyncTask<Term, Void, Result<List<Subject>>>() {
        override fun doInBackground(vararg params: Term?): Result<List<Subject>>? {
            if (params.size != 1) {
                return null
            }
            val term = params[0] ?: return null
            return subjectRepository.getDataFromNet(term)
        }

        override fun onPostExecute(result: Result<List<Subject>>?) {
            super.onPostExecute(result)
            subjectRepository.subjectLiveData.value = result
        }
    }

}