package com.qianlei.jiaowu.ui.fragment.subject

import android.app.Application
import android.os.Handler
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.qianlei.jiaowu.common.Result
import com.qianlei.jiaowu.common.ResultType
import com.qianlei.jiaowu.db.MyDataBase
import com.qianlei.jiaowu.db.dao.SubjectDao
import com.qianlei.jiaowu.entity.Subject
import com.qianlei.jiaowu.net.StudentApi
import java.util.concurrent.*

/**
 * @author qianlei
 */
class SubjectViewModel(private val app: Application) : AndroidViewModel(app) {
    private val executor: ExecutorService = ThreadPoolExecutor(1, 1,
            0, TimeUnit.SECONDS, SynchronousQueue(), ThreadFactory { r: Runnable? -> Thread(r, "获取课程进程") },
            ThreadPoolExecutor.DiscardPolicy())
    private val handler = Handler()
    val subjectResult = MutableLiveData<Result<List<Subject>>>()
    private var subjectDao: SubjectDao
    private val studentApi: StudentApi = StudentApi.getStudentApi(app)
    private var getFromDatabase = true

    /**
     * 刷新数据
     *
     * @param year 学年
     * @param term 学期
     */
    fun refreshData(year: String, term: String) {
        executor.execute {
            if (getFromDatabase) {
                getFromDatabase = false
                val subject = getDataFromDatabase(year, term)
                if (subject.isSuccess()) {
                    handler.post { subjectResult.setValue(subject) }
                    return@execute
                }
            }
            val result = getDataFromNet(year, term)
            handler.post { subjectResult.setValue(result) }
        }
    }

    fun changeTerm(year: String, term: String) {
        getFromDatabase = true
        refreshData(year, term)
    }

    private fun getDataFromDatabase(year: String, term: String): Result<List<Subject>> {
        subjectDao = MyDataBase.getDatabase(app).subjectDao()
        val subjectList = subjectDao.selectAllSubjectByYearAndTerm(year, term)
        return if (subjectList.isNotEmpty()) {
            Result(ResultType.OK, "从数据获取成功", subjectList)
        } else {
            Result(ResultType.OTHER, "数据库中无数据")
        }
    }

    private fun getDataFromNet(year: String, term: String): Result<List<Subject>> {
        val result = studentApi.getStudentTimetable(year, term)
        if (result.isSuccess()) { //向数据库中添加数据
            subjectDao = MyDataBase.getDatabase(app).subjectDao()
            val subjectList = result.data
            if (subjectList != null) {
                subjectDao.deleteAllSubjectByYearAndTerm(year, term)
                for (subject in subjectList) {
                    subjectDao.insertSubject(subject)
                }
            }
        }
        return result
    }

    init {
        subjectDao = MyDataBase.getDatabase(app).subjectDao()
    }
}