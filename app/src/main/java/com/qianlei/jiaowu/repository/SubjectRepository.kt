package com.qianlei.jiaowu.repository

import android.content.Context
import com.qianlei.jiaowu.common.Result
import com.qianlei.jiaowu.common.ResultType
import com.qianlei.jiaowu.entity.Term
import com.qianlei.jiaowu.db.MyDataBase
import com.qianlei.jiaowu.db.dao.SubjectDao
import com.qianlei.jiaowu.entity.Subject
import com.qianlei.jiaowu.net.StudentClient

/**
 * 课程的仓库类
 *
 * @author qianlei
 */
object SubjectRepository {

    private suspend fun getDataFromDatabase(context: Context, term: Term): Result<List<Subject>> {
        val subjectDao: SubjectDao = MyDataBase.getDatabase(context).subjectDao()
        val subjectList = subjectDao.selectAllSubjectByYearAndTerm(term.year, term.term)
        return if (subjectList.isNotEmpty()) {
            Result(ResultType.OK, "从数据获取成功", subjectList)
        } else {
            Result(ResultType.OTHER, "数据库中无数据")
        }
    }

    private suspend fun getDataFromNet(context: Context, term: Term): Result<List<Subject>> {
        val subjectDao: SubjectDao = MyDataBase.getDatabase(context).subjectDao()
        val result = StudentClient.getStudentTimetable(context, term.year, term.term)
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

    suspend fun getSubjectDataUseCache(context: Context, term: Term): Result<List<Subject>> {
        val result = getDataFromDatabase(context, term)
        return if (result.isSuccess()) {
            result
        } else {
            getDataFromNet(context, term)
        }
    }

    suspend fun getSubjectDataNotUseCache(context: Context, term: Term): Result<List<Subject>> {
        return getDataFromNet(context, term)
    }
}