package com.qianlei.jiaowu.repository

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import com.qianlei.jiaowu.common.Result
import com.qianlei.jiaowu.entity.Student
import com.qianlei.jiaowu.net.StudentClient

class StudentRepository constructor(context: Context) {

    private val studentClient: StudentClient = StudentClient.getStudentApi(context)

    companion object {
        val studentData = MutableLiveData<Result<Student>>()
    }

    class GetStudentInformationTask constructor(private val studentRepository: StudentRepository) : AsyncTask<Void, Void, Result<Student>>() {
        override fun doInBackground(vararg params: Void?): Result<Student> {
            return studentRepository.studentClient.getStudentInformation()
        }

        override fun onPostExecute(result: Result<Student>?) {
            super.onPostExecute(result)
            studentData.value = result
        }
    }

}