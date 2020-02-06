package com.qianlei.jiaowu.net

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.preference.PreferenceManager
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONException
import com.alibaba.fastjson.JSONObject
import com.qianlei.jiaowu.common.Result
import com.qianlei.jiaowu.common.ResultType
import com.qianlei.jiaowu.entity.Examination
import com.qianlei.jiaowu.entity.Score
import com.qianlei.jiaowu.entity.Student
import com.qianlei.jiaowu.entity.Subject
import com.qianlei.jiaowu.utils.Base64
import com.qianlei.jiaowu.utils.RSAEncoder
import org.jsoup.Connection
import org.jsoup.Jsoup
import java.io.IOException
import java.util.*

/**
 * 从教务处获取信息
 *
 * @author qianlei
 */
class StudentApi private constructor(private val context: Context) {
    /**
     * 临时的cookies 登录之前使用
     */
    private var tempCookies: Map<String, String>? = null
    /**
     * 在登录之后使用的cookies
     */
    private var lastLoginCookies: Map<String, String>? = null
    private var csrftoken: String? = null

    /**
     * 获取公钥并加密密码
     * 登陆用的密码需要先用RSA加密，然后转换为Base64的格式
     *
     * @param password 加密前的密码
     * @return 加密后的密码
     * @throws IOException IO异常
     */
    @Throws(IOException::class)
    private fun getRsaPublicKey(password: String?): String? {
        if (password == null) {
            return null
        }
        var retPassword = password
        val connection = Jsoup.connect("$prefix/jwglxt/xtgl/login_getPublicKey.html")
        val response = connection.cookies(tempCookies).ignoreContentType(true).execute()
        val jsonObject = JSON.parseObject(response.body())
        val modulus = jsonObject.getString("modulus")
        val exponent = jsonObject.getString("exponent")
        retPassword = RSAEncoder.rsaEncrypt(retPassword, Base64.b64toHex(modulus), Base64.b64toHex(exponent))
        retPassword = Base64.hex2b64(retPassword)
        return retPassword
    }

    /**
     * 学生登录
     *
     * @param studentId 学号
     * @param password  密码
     * @param code      验证码
     * @return 登录结果
     */
    fun login(studentId: String?, password: String?, code: String?): Result<String> {
        var tempPassword = password
        return try {
            tempPassword = getRsaPublicKey(tempPassword)
            val connection = Jsoup.connect("$prefix/jwglxt/xtgl/login_slogin.html")
            connection.header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
            connection.data("csrftoken", csrftoken)
            connection.data("yhm", studentId)
            connection.data("mm", tempPassword)
            connection.data("mm", tempPassword)
            connection.data("yzm", code)
            val response = connection.cookies(tempCookies).ignoreContentType(true)
                    .method(Connection.Method.POST).execute()
            val document = Jsoup.parse(response.body())
            if (document.getElementById("tips") == null) { //设置cookie
                lastLoginCookies = response.cookies()
                Result(ResultType.OK, "成功登陆")
            } else {
                Result(ResultType.PARAMS_ERROR, document.getElementById("tips").text())
            }
        } catch (e: IOException) {
            Result(ResultType.IO, "请检查网络连接")
        } catch (e: Exception) {
            Result(ResultType.IO, "其他错误" + e.message)
        }
    }

    /**
     * 获取验证码
     *
     * @return 获取到的验证码的结果
     */
    fun getCaptchaImage(): Result<Bitmap> {
        try {
            val connection = Jsoup.connect("$prefix/jwglxt/xtgl/login_slogin.html")
            val response = connection.execute()
            //保存cookie
            tempCookies = response.cookies()
            //保存csrftoken
            val document = Jsoup.parse(response.body())
            csrftoken = document.getElementById("csrftoken").`val`()
        } catch (e: IOException) {
            return Result(ResultType.IO, "请检查网络连接")
        }
        //获取验证码
        val connection = Jsoup.connect("$prefix/jwglxt/kaptcha").ignoreContentType(true)
        return try {
            val response = connection.cookies(tempCookies).execute()
            val bytes = response.bodyAsBytes()
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            Result(ResultType.OK, "获取成功", bitmap)
        } catch (e: IOException) {
            Result(ResultType.IO, "请检查网络连接")
        } catch (e: Exception) {
            Result(ResultType.OTHER, "其他错误" + e.message)
        }
    }

    /**
     * 查询学生信息
     *
     * @return 获取到的学生的信息的结果
     */
    fun getStudentInformation(): Result<Student> {
        return if (lastLoginCookies == null) {
            Result(ResultType.NEED_LOGIN, "请先登陆")
        } else try {
            val connection = Jsoup.connect("$prefix/jwglxt/xsxxxggl/xsxxwh_cxCkDgxsxx.html?gnmkdm=N100801")
            val response = connection.cookies(lastLoginCookies).method(Connection.Method.GET).ignoreContentType(true).execute()
            val student = JSON.parseObject(response.body(), Student::class.java)
            Result(ResultType.OK, "获取信息成功", student)
        } catch (e: JSONException) {
            Result<Student>(ResultType.NEED_LOGIN, "请重新登陆")
        } catch (e: IOException) {
            Result<Student>(ResultType.IO, "请检查网络连接")
        } catch (e: Exception) {
            Result<Student>(ResultType.OTHER, "其他错误" + e.message)
        }
    }

    /**
     * 获取课表信息
     *
     * @param year 学年 只需要传入一个 例如2018-2019学年只需要传入2018
     * @param term 学期
     * @return 当学期的课表信息
     */
    fun getStudentTimetable(year: String?, term: String?): Result<List<Subject>> {
        return if (lastLoginCookies == null) {
            Result(ResultType.NEED_LOGIN, "请先登陆")
        } else try {
            val connection = Jsoup.connect("$prefix/jwglxt/kbcx/xskbcx_cxXsKb.html?gnmkdm=N2151")
            connection.data("xnm", year)
            connection.data("xqm", term)
            val response: Connection.Response
            response = connection.cookies(lastLoginCookies).method(Connection.Method.POST).ignoreContentType(true).execute()
            val body = JSON.parseObject(response.body())
            val timeTable = JSON.parseArray(body.getString("kbList"))
            val subjectList: MutableList<Subject> = ArrayList()
            for (o in timeTable) {
                val s = (o as JSONObject).toJSONString()
                val subject = JSON.parseObject(s, Subject::class.java)
                subjectList.add(subject)
            }
            Result<List<Subject>>(ResultType.OK, "获取成功", subjectList)
        } catch (e: IOException) {
            Result<List<Subject>>(ResultType.IO, "请检查网络连接")
        } catch (e: JSONException) {
            Result<List<Subject>>(ResultType.NEED_LOGIN, "请重新登陆")
        } catch (e: Exception) {
            Result<List<Subject>>(ResultType.OTHER, "其他错误" + e.message)
        }
    }

    /**
     * 获取成绩信息
     *
     * @param year 学年 只需要传入一个 例如2018-2019学年只需要传入2018
     * @param term 学期
     * @return 当学期的成绩信息
     */
    fun getStudentScore(year: String, term: String): Result<List<Score>> {
        return if (lastLoginCookies == null) {
            Result(ResultType.NEED_LOGIN, "请先登陆")
        } else try {
            val parameter: MutableMap<String, String> = HashMap(2)
            parameter["xnm"] = year
            parameter["xqm"] = term
            val connection = Jsoup.connect("$prefix/jwglxt/cjcx/cjcx_cxDgXscj.html?doType=query&gnmkdm=N305005")
            val response = connection.cookies(lastLoginCookies).method(Connection.Method.POST)
                    .data(parameter).ignoreContentType(true).execute()
            val jsonObject = JSON.parseObject(response.body())
            val gradeTable = JSON.parseArray(jsonObject.getString("items"))
            val scoreList: MutableList<Score> = ArrayList()
            for (o in gradeTable) {
                val lesson = o as JSONObject
                val ans = JSON.parseObject(lesson.toJSONString(), Score::class.java)
                scoreList.add(ans)
            }
            Result<List<Score>>(ResultType.OK, "获取成功", scoreList)
        } catch (e: IOException) {
            Result<List<Score>>(ResultType.IO, "请检查网络连接")
        } catch (e: JSONException) {
            Result<List<Score>>(ResultType.NEED_LOGIN, "请重新登陆")
        } catch (e: Exception) {
            Result<List<Score>>(ResultType.OTHER, "其他错误" + e.message)
        }
    }

    fun getStudentExamInformation(year: String, term: String): Result<List<Examination>> {
        return if (lastLoginCookies == null) {
            Result(ResultType.NEED_LOGIN, "请先登陆")
        } else try {
            val parameter: MutableMap<String, String> = HashMap(2)
            parameter["xnm"] = year
            parameter["xqm"] = term
            val connection = Jsoup.connect("$prefix/jwglxt/kwgl/kscx_cxXsksxxIndex.html?doType=query&gnmkdm=N358105")
            val response = connection.cookies(lastLoginCookies).method(Connection.Method.POST)
                    .data(parameter).ignoreContentType(true).execute()
            val jsonObject = JSON.parseObject(response.body())
            val examTable = JSON.parseArray(jsonObject.getString("items"))
            val examinationList: MutableList<Examination> = ArrayList()
            for (o in examTable) {
                val exam = o as JSONObject
                val ans = JSON.parseObject(exam.toJSONString(), Examination::class.java)
                ans.year = year
                ans.term = term
                examinationList.add(ans)
            }
            Result<List<Examination>>(ResultType.OK, "获取成功", examinationList)
        } catch (e: IOException) {
            Result<List<Examination>>(ResultType.IO, "请检查网络连接")
        } catch (e: JSONException) {
            Result<List<Examination>>(ResultType.NEED_LOGIN, "请重新登陆")
        } catch (e: Exception) {
            Result<List<Examination>>(ResultType.OTHER, "其他错误")
        }
    }

    /**
     * 读取cookies
     */
    @Suppress("UNCHECKED_CAST")
    private fun readCookies() {
        val sharedPreferences = context.getSharedPreferences("cookies", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("cookies", "")
        val map: Map<String, String> = JSON.parseObject<Map<*, *>>(json, MutableMap::class.java) as Map<String, String>
        lastLoginCookies = map
    }

    /**
     * 保存cookies
     */
    fun saveCookies() {
        val json = JSON.toJSON(lastLoginCookies)
        if (json != null) {
            val sharedPreferences = context.getSharedPreferences("cookies", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("cookies", json.toString())
            editor.apply()
        }
    }

    private val prefix: String
        get() {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return if (sharedPreferences.getBoolean("useIntranet", false)) {
                "http://www.gdjw.zjut.edu.cn"
            } else {
                //TODO 内网地址暂不知 回校测试
                "http://www.gdjw.zjut.edu.cn"
            }
        }

    companion object {
        /**
         * 保证cookie唯一 需要单例
         * 这里获取的context是application的context不会内存泄漏
         */
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var studentApi: StudentApi? = null

        fun getStudentApi(context: Context): StudentApi {
            if (studentApi == null) {
                synchronized(StudentApi::class.java) {
                    if (studentApi == null) {
                        studentApi = StudentApi(context.applicationContext)
                    }
                }
            }
            return studentApi!!
        }
    }

    init {
        readCookies()
    }
}