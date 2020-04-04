package com.qianlei.jiaowu.net

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.qianlei.jiaowu.R
import com.qianlei.jiaowu.common.Result
import com.qianlei.jiaowu.common.ResultType
import com.qianlei.jiaowu.entity.Examination
import com.qianlei.jiaowu.entity.Score
import com.qianlei.jiaowu.entity.Subject
import com.qianlei.jiaowu.utils.Base64
import com.qianlei.jiaowu.utils.RSAEncoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Connection
import org.jsoup.Jsoup
import java.io.IOException
import java.util.*

/**
 * 从教务处获取信息
 *
 * @author qianlei
 */
object StudentClient {
    private val TAG = "STUDENT_CLIENT"
    private val gson = Gson()

    /**
     * 临时的cookies 登录之前使用
     */
    @Volatile
    private var tempCookies: Map<String, String>? = null

    /**
     * 在登录之后使用的cookies
     */
    @Volatile
    private var lastLoginCookies: Map<String, String>? = null

    @Volatile
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
    private suspend fun getRsaPublicKey(context: Context, password: String?): String? {
        if (password == null) {
            return null
        }
        val response = withContext(Dispatchers.IO) {
            val connection = Jsoup.connect(prefix(context) + "/jwglxt/xtgl/login_getPublicKey.html")
            return@withContext connection.cookies(tempCookies).ignoreContentType(true).execute()
        }
        return withContext(Dispatchers.Default) {
            var retPassword: String = password
            val jsonObject = JsonParser.parseString(response.body()).asJsonObject
            val modulus = jsonObject.get("modulus").asString
            val exponent = jsonObject.get("exponent").asString
            retPassword = RSAEncoder.rsaEncrypt(retPassword, Base64.b64toHex(modulus), Base64.b64toHex(exponent))
            retPassword = Base64.hex2b64(retPassword)
            return@withContext retPassword
        }
    }

    /**
     * 学生登录
     *
     * @param studentId 学号
     * @param password  密码
     * @param code      验证码
     * @return 登录结果
     */
    suspend fun login(context: Context, studentId: String?, password: String?, code: String?): Result<String> {
        return withContext(Dispatchers.IO) {
            var tempPassword = password
            try {
                tempPassword = getRsaPublicKey(context, tempPassword)
                val connection = Jsoup.connect(prefix(context) + "/jwglxt/xtgl/login_slogin.html?time=" + System.currentTimeMillis())
                connection.header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                connection.data("csrftoken", csrftoken)
                connection.data("yhm", studentId)
                connection.data("mm", tempPassword)
                connection.data("mm", tempPassword)
                connection.data("yzm", code)
                val response = connection.cookies(tempCookies).ignoreContentType(true)
                        .method(Connection.Method.POST).execute()
                return@withContext withContext(Dispatchers.Default) {
                    val document = Jsoup.parse(response.body())
                    if (document.getElementById("tips") == null) { //设置cookie
                        lastLoginCookies = response.cookies()
                        Result<String>(ResultType.OK, "成功登陆")
                    } else {
                        Result(ResultType.PARAMS_ERROR, document.getElementById("tips").text())
                    }
                }
            } catch (e: IOException) {
                Result<String>(ResultType.IO, "请检查网络连接")
            } catch (e: Exception) {
                Log.e(TAG, "登陆错误", e)
                Result<String>(ResultType.IO, "其他错误" + e.message)
            }
        }
    }

    /**
     * 获取验证码
     *
     * @return 获取到的验证码的结果
     */
    suspend fun getCaptchaImage(context: Context): Result<Bitmap> {
        return withContext(Dispatchers.IO) {
            try {
                try {
                    val connection = Jsoup.connect(prefix(context) + "/jwglxt/xtgl/login_slogin.html?time" + System.currentTimeMillis())
                    val response = connection.execute()
                    //保存cookie
                    tempCookies = response.cookies()
                    //保存csrftoken
                    val document = Jsoup.parse(response.body())
                    csrftoken = document.getElementById("csrftoken").`val`()
                } catch (e: IOException) {
                    return@withContext Result<Bitmap>(ResultType.IO, "请检查网络连接")
                }
                val connection = Jsoup.connect(prefix(context) + "/jwglxt/kaptcha").ignoreContentType(true)
                val response = connection.cookies(tempCookies).execute()
                withContext(Dispatchers.Default) {
                    val bytes = response.bodyAsBytes()
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    Result<Bitmap>(ResultType.OK, "获取成功", bitmap)
                }
            } catch (e: IOException) {
                return@withContext Result<Bitmap>(ResultType.IO, "请检查网络连接")
            } catch (e: Exception) {
                return@withContext Result<Bitmap>(ResultType.OTHER, "其他错误" + e.message)
            }
        }
    }

    /**
     * 获取课表信息
     *
     * @param year 学年 只需要传入一个 例如2018-2019学年只需要传入2018
     * @param term 学期
     * @return 当学期的课表信息
     */
    suspend fun getStudentTimetable(context: Context, year: String?, term: String?): Result<List<Subject>> {
        if (lastLoginCookies == null) {
            return Result(ResultType.NEED_LOGIN, "请先登陆")
        }
        return withContext(Dispatchers.IO) {
            try {
                val connection = Jsoup.connect(prefix(context) + "/jwglxt/kbcx/xskbcx_cxXsKb.html?gnmkdm=N2151")
                connection.data("xnm", year)
                connection.data("xqm", term)
                val response: Connection.Response
                response = connection.cookies(lastLoginCookies)
                        .method(Connection.Method.POST).ignoreContentType(true).execute()
                withContext(Dispatchers.Default) {
                    val body = JsonParser.parseString(response.body()).asJsonObject
                    val subjectList = gson.fromJson<List<Subject>>(body.get("kbList")
                            , object : TypeToken<List<Subject>>() {}.type)
                    Result<List<Subject>>(ResultType.OK, "获取成功", subjectList)
                }
            } catch (e: IOException) {
                Result<List<Subject>>(ResultType.IO, "请检查网络连接")
            } catch (e: JsonSyntaxException) {
                Result<List<Subject>>(ResultType.NEED_LOGIN, "请重新登陆")
            } catch (e: Exception) {
                Result<List<Subject>>(ResultType.OTHER, "其他错误" + e.message)
            }
        }
    }

    /**
     * 获取成绩信息
     *
     * @param year 学年 只需要传入一个 例如2018-2019学年只需要传入2018
     * @param term 学期
     * @return 当学期的成绩信息
     */
    suspend fun getStudentScore(context: Context, year: String, term: String): Result<List<Score>> {
        if (lastLoginCookies == null) {
            return Result(ResultType.NEED_LOGIN, "请先登陆")
        }
        return withContext(Dispatchers.IO) {
            try {
                val parameter: MutableMap<String, String> = HashMap(2)
                parameter["xnm"] = year
                parameter["xqm"] = term
                val connection = Jsoup.connect(prefix(context) + "/jwglxt/cjcx/cjcx_cxDgXscj.html?doType=query&gnmkdm=N305005")
                val response = connection.cookies(lastLoginCookies).method(Connection.Method.POST)
                        .data(parameter).ignoreContentType(true).execute()
                withContext(Dispatchers.Default) {
                    val jsonObject = JsonParser.parseString(response.body()).asJsonObject
                    val scoreList = gson.fromJson<List<Score>>(jsonObject.get("items")
                            , object : TypeToken<List<Score>>() {}.type)
                    Result(ResultType.OK, "获取成功", scoreList)
                }
            } catch (e: IOException) {
                Result<List<Score>>(ResultType.IO, "请检查网络连接")
            } catch (e: JsonSyntaxException) {
                Result<List<Score>>(ResultType.NEED_LOGIN, "请重新登陆")
            } catch (e: Exception) {
                Result<List<Score>>(ResultType.OTHER, "其他错误" + e.message)
            }
        }
    }

    suspend fun getStudentExamInformation(context: Context, year: String, term: String): Result<List<Examination>> {
        if (lastLoginCookies == null) {
            return Result(ResultType.NEED_LOGIN, "请先登陆")
        }
        return withContext(Dispatchers.IO) {
            try {
                val parameter: MutableMap<String, String> = HashMap(2)
                parameter["xnm"] = year
                parameter["xqm"] = term
                val connection = Jsoup.connect(prefix(context) + "/jwglxt/kwgl/kscx_cxXsksxxIndex.html?doType=query&gnmkdm=N358105")
                val response = connection.cookies(lastLoginCookies)
                        .method(Connection.Method.POST)
                        .data(parameter).ignoreContentType(true).execute()
                withContext(Dispatchers.Default) {
                    val jsonObject = JsonParser.parseString(response.body()).asJsonObject
                    val examinationList = gson.fromJson<List<Examination>>(jsonObject.get("items")
                            , object : TypeToken<List<Examination>>() {}.type)
                    Result<List<Examination>>(ResultType.OK, "获取成功", examinationList)
                }
            } catch (e: IOException) {
                Result<List<Examination>>(ResultType.IO, "请检查网络连接")
            } catch (e: JsonSyntaxException) {
                Result<List<Examination>>(ResultType.NEED_LOGIN, "请重新登陆")
            } catch (e: Exception) {
                Result<List<Examination>>(ResultType.OTHER, "其他错误")
            }
        }
    }

    /**
     * 读取cookies
     */
    fun readCookies(context: Context) {
        val sharedPreferences = context.getSharedPreferences("cookies", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("cookies", "")
        lastLoginCookies = gson.fromJson<Map<String, String>>(json, object : TypeToken<Map<String, String>>() {}.type)
    }

    /**
     * 保存cookies
     */
    fun saveCookies(context: Context) {
        val json = gson.toJson(lastLoginCookies)
        if (json != null) {
            val sharedPreferences = context.getSharedPreferences("cookies", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("cookies", json.toString())
            editor.apply()
        }
    }

    private fun prefix(context: Context): String {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return if (sharedPreferences.getBoolean(context.getString(R.string.useIntranet), false)) {
            "http://www.gdjw.zjut.edu.cn"
        } else {
            //TODO 内网地址暂不知 回校测试
            "http://www.gdjw.zjut.edu.cn"
        }
    }
}