package com.qianlei.jiaowu.repository

import android.content.Context
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
object ExamRepository {

    private suspend fun getDataFromNet(context: Context, term: Term): Result<List<Examination>> {
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

    private suspend fun getDataFromDatabase(context: Context, term: Term): Result<List<Examination>> {
        val examDao: ExamDao = MyDataBase.getDatabase(context).examDao()
        val examinationList = examDao.selectAllExamByYearAndTerm(term.year, term.term)
        return if (examinationList.isNotEmpty()) {
            Result(ResultType.OK, "从数据获取成功", examinationList)
        } else {
            Result(ResultType.OTHER, "数据库中无数据")
        }
    }

    suspend fun getExamDataUseCache(context: Context, term: Term): Result<List<Examination>> {
        val result = getDataFromDatabase(context, term)
        return if (result.isSuccess()) {
            result
        } else {
            getDataFromNet(context, term)
        }
    }

    suspend fun getExamDataNotUseCache(context: Context, term: Term): Result<List<Examination>> {
        return getDataFromNet(context, term)
    }

}