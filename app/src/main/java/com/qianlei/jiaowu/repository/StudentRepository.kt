package com.qianlei.jiaowu.repository

import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import com.qianlei.jiaowu.MainApplication
import com.qianlei.jiaowu.common.Result
import com.qianlei.jiaowu.entity.Student
import com.qianlei.jiaowu.net.StudentApi

object StudentRepository {
    val studentData = MutableLiveData<Result<Student>>()
    private val studentApi: StudentApi = StudentApi.getStudentApi(MainApplication.getInstance())

    class GetStudentInformationTask : AsyncTask<Void, Void, Result<Student>>() {
        override fun doInBackground(vararg params: Void?): Result<Student> {
            return studentApi.getStudentInformation()
        }

        override fun onPostExecute(result: Result<Student>?) {
            super.onPostExecute(result)
            studentData.value = result
        }
    }

}