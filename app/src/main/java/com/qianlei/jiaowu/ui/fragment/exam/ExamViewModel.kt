package com.qianlei.jiaowu.ui.fragment.exam

import android.app.Application
import android.os.Handler
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.qianlei.jiaowu.common.Result
import com.qianlei.jiaowu.common.ResultType
import com.qianlei.jiaowu.db.MyDataBase
import com.qianlei.jiaowu.db.dao.ExamDao
import com.qianlei.jiaowu.entity.Examination
import com.qianlei.jiaowu.net.StudentApi
import java.util.concurrent.*

/**
 * 考试信息相关的ViewModel
 *
 * @author qianlei
 */
class ExamViewModel(app: Application) : AndroidViewModel(app) {
    private val executorService: ExecutorService = ThreadPoolExecutor(1, 1,
            0, TimeUnit.MINUTES, SynchronousQueue(), ThreadFactory { r: Runnable? -> Thread(r, "获取考试信息线程") },
            ThreadPoolExecutor.DiscardPolicy())
    private val handler = Handler()
    private var getDataFromDataBase = true
    private val examDao: ExamDao = MyDataBase.getDatabase(getApplication()).examDao()
    private val studentApi: StudentApi = StudentApi.getStudentApi(getApplication())
    val examListData = MutableLiveData<Result<List<Examination>>>()

    /**
     * 刷新数据
     *
     * @param year 学年
     * @param term 学期
     */
    fun refreshData(year: String, term: String) {
        executorService.submit {
            //首次从本地数据库中获取数据 之后从网络中获取数据
            if (getDataFromDataBase) {
                getDataFromDataBase = false
                val result = getDataFromDatabase(year, term)
                if (result.isSuccess()) {
                    handler.post { examListData.setValue(result) }
                    return@submit
                }
            }
            val result = getDataFromNet(year, term)
            handler.post { examListData.setValue(result) }
        }
    }

    /**
     * 修改学期
     *
     * @param year 修改好的学年
     * @param term 修改后的学期
     */
    fun changeTerm(year: String, term: String) {
        getDataFromDataBase = true
        refreshData(year, term)
    }

    private fun getDataFromNet(year: String, term: String): Result<List<Examination>> {
        val result = studentApi.getStudentExamInformation(year, term)
        if (result.isSuccess()) { //向数据库中添加数据
            val examDao: ExamDao = MyDataBase.getDatabase(getApplication()).examDao()
            val examinationList = result.data
            examDao.deleteAllByYearAndTerm(year, term)
            if (examinationList != null) {
                for (examination in examinationList) {
                    examDao.insertExam(examination)
                }
            }
        }
        return result
    }

    private fun getDataFromDatabase(year: String, term: String): Result<List<Examination>> {
        val examinationList = examDao.selectAllExamByYearAndTerm(year, term)
        return if (examinationList != null && examinationList.isNotEmpty()) {
            Result(ResultType.OK, "从数据获取成功", examinationList)
        } else {
            Result(ResultType.OTHER, "数据库中无数据")
        }
    }

}