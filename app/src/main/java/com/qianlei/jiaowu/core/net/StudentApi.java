package com.qianlei.jiaowu.core.net;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.preference.PreferenceManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.qianlei.jiaowu.common.Result;
import com.qianlei.jiaowu.common.ResultType;
import com.qianlei.jiaowu.entity.Examination;
import com.qianlei.jiaowu.entity.Score;
import com.qianlei.jiaowu.entity.Student;
import com.qianlei.jiaowu.entity.Subject;
import com.qianlei.jiaowu.utils.Base64;
import com.qianlei.jiaowu.utils.RSAEncoder;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 从教务处获取信息
 *
 * @author qianlei
 */
public class StudentApi {
    /**
     * 保证cookie唯一 需要单例
     * 这里获取的context是application的context不会内存泄漏
     */
    @SuppressLint("StaticFieldLeak")
    private static volatile StudentApi studentApi;
    /**
     * 临时的cookies 登录之前使用
     */
    private Map<String, String> tempCookies;
    /**
     * 在登录之后使用的cookies
     */
    private Map<String, String> lastLoginCookies;
    private String csrftoken;
    private Context context;

    private StudentApi(Context context) {
        this.context = context;
        readCookies();
    }

    public static StudentApi getStudentApi(Context context) {
        if (studentApi == null) {
            synchronized (StudentApi.class) {
                if (studentApi == null) {
                    studentApi = new StudentApi(context.getApplicationContext());
                }
            }
        }
        return studentApi;
    }

    /**
     * 获取公钥并加密密码
     * 登陆用的密码需要先用RSA加密，然后转换为Base64的格式
     *
     * @param password 加密前的密码
     * @return 加密后的密码
     * @throws IOException IO异常
     */
    private String getRsaPublicKey(String password) throws IOException {
        Connection connection = Jsoup.connect(getPrefix() + "/jwglxt/xtgl/login_getPublicKey.html");
        Connection.Response response = connection.cookies(tempCookies).ignoreContentType(true).execute();
        JSONObject jsonObject = JSON.parseObject(response.body());
        String modulus = jsonObject.getString("modulus");
        String exponent = jsonObject.getString("exponent");
        password = RSAEncoder.RSAEncrypt(password, Base64.b64tohex(modulus), Base64.b64tohex(exponent));
        password = Base64.hex2b64(password);
        return password;
    }

    /**
     * 学生登录
     *
     * @param studentId 学号
     * @param password  密码
     * @param code      验证码
     * @return 登录结果
     */
    public Result<String> login(String studentId, String password, String code) {
        try {
            password = getRsaPublicKey(password);
            Connection connection = Jsoup.connect(getPrefix() + "/jwglxt/xtgl/login_slogin.html");
            connection.header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            connection.data("csrftoken", csrftoken);
            connection.data("yhm", studentId);
            connection.data("mm", password);
            connection.data("mm", password);
            connection.data("yzm", code);
            Connection.Response response = connection.cookies(tempCookies).ignoreContentType(true)
                    .method(Connection.Method.POST).execute();

            Document document = Jsoup.parse(response.body());
            if (document.getElementById("tips") == null) {
                //设置cookie
                lastLoginCookies = response.cookies();
                return new Result<>(ResultType.OK, "成功登陆");
            } else {
                return new Result<>(ResultType.PARAMS_ERROR, document.getElementById("tips").text());
            }
        } catch (IOException e) {
            return new Result<>(ResultType.IO, "请检查网络连接");
        } catch (Exception e) {
            return new Result<>(ResultType.IO, "其他错误" + e.getMessage());
        }
    }

    /**
     * 获取验证码
     *
     * @return 获取到的验证码的结果
     */
    public Result<Bitmap> getCaptchaImage() {
        try {
            Connection connection = Jsoup.connect(getPrefix() + "/jwglxt/xtgl/login_slogin.html");
            Connection.Response response = connection.execute();
            //保存cookie
            tempCookies = response.cookies();
            //保存csrftoken
            Document document = Jsoup.parse(response.body());
            csrftoken = document.getElementById("csrftoken").val();
        } catch (IOException e) {
            return new Result<>(ResultType.IO, "请检查网络连接");
        }
        //获取验证码
        Connection connection = Jsoup.connect(getPrefix() + "/jwglxt/kaptcha").ignoreContentType(true);
        try {
            Connection.Response response = connection.cookies(tempCookies).execute();
            byte[] bytes = response.bodyAsBytes();
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            return new Result<>(ResultType.OK, "获取成功", bitmap);
        } catch (IOException e) {
            return new Result<>(ResultType.IO, "请检查网络连接");
        } catch (Exception e) {
            return new Result<>(ResultType.OTHER, "其他错误" + e.getMessage());
        }
    }

    /**
     * 查询学生信息
     *
     * @return 获取到的学生的信息的结果
     */
    public Result<Student> getStudentInformation() {
        if (lastLoginCookies == null) {
            return new Result<>(ResultType.NEED_LOGIN, "请先登陆");
        }
        try {
            Connection connection = Jsoup.connect(getPrefix() + "/jwglxt/xsxxxggl/xsxxwh_cxCkDgxsxx.html?gnmkdm=N100801");
            Connection.Response response = connection.cookies(lastLoginCookies).method(Connection.Method.GET).ignoreContentType(true).execute();
            Student student = JSON.parseObject(response.body(), Student.class);
            return new Result<>(ResultType.OK, "获取信息成功", student);
        } catch (JSONException e) {
            return new Result<>(ResultType.NEED_LOGIN, "请重新登陆");
        } catch (IOException e) {
            return new Result<>(ResultType.IO, "请检查网络连接");
        } catch (Exception e) {
            return new Result<>(ResultType.OTHER, "其他错误" + e.getMessage());
        }
    }

    /**
     * 获取课表信息
     *
     * @param year 学年 只需要传入一个 例如2018-2019学年只需要传入2018
     * @param term 学期
     * @return 当学期的课表信息
     */
    public Result<List<Subject>> getStudentTimetable(String year, String term) {
        if (lastLoginCookies == null) {
            return new Result<>(ResultType.NEED_LOGIN, "请先登陆");
        }
        try {
            Connection connection = Jsoup.connect(getPrefix() + "/jwglxt/kbcx/xskbcx_cxXsKb.html?gnmkdm=N2151");
            connection.data("xnm", year);
            connection.data("xqm", term);
            Connection.Response response;
            response = connection.cookies(lastLoginCookies).
                    method(Connection.Method.POST).ignoreContentType(true).execute();
            JSONObject body = JSON.parseObject(response.body());
            JSONArray timeTable = JSON.parseArray(body.getString("kbList"));
            List<Subject> subjectList = new ArrayList<>();
            for (Object o : timeTable) {
                String s = ((JSONObject) o).toJSONString();
                Subject subject = JSON.parseObject(s, Subject.class);
                subjectList.add(subject);
            }
            return new Result<>(ResultType.OK, "获取成功", subjectList);
        } catch (IOException e) {
            return new Result<>(ResultType.IO, "请检查网络连接");
        } catch (JSONException e) {
            return new Result<>(ResultType.NEED_LOGIN, "请重新登陆");
        } catch (Exception e) {
            return new Result<>(ResultType.OTHER, "其他错误" + e.getMessage());
        }
    }

    /**
     * 获取成绩信息
     *
     * @param year 学年 只需要传入一个 例如2018-2019学年只需要传入2018
     * @param term 学期
     * @return 当学期的成绩信息
     */
    public Result<List<Score>> getStudentScore(String year, String term) {
        if (lastLoginCookies == null) {
            return new Result<>(ResultType.NEED_LOGIN, "请先登陆");
        }
        try {
            Map<String, String> parameter = new HashMap<>(2);
            parameter.put("xnm", year);
            parameter.put("xqm", term);
            Connection connection = Jsoup.connect(getPrefix() + "/jwglxt/cjcx/cjcx_cxDgXscj.html?doType=query&gnmkdm=N305005");
            Connection.Response response = connection.cookies(lastLoginCookies).method(Connection.Method.POST)
                    .data(parameter).ignoreContentType(true).execute();
            JSONObject jsonObject = JSON.parseObject(response.body());
            JSONArray gradeTable = JSON.parseArray(jsonObject.getString("items"));
            List<Score> scoreList = new ArrayList<>();
            for (Object o : gradeTable) {
                JSONObject lesson = (JSONObject) o;
                Score ans = JSON.parseObject(lesson.toJSONString(), Score.class);
                scoreList.add(ans);
            }
            return new Result<>(ResultType.OK, "获取成功", scoreList);
        } catch (IOException e) {
            return new Result<>(ResultType.IO, "请检查网络连接");
        } catch (JSONException e) {
            return new Result<>(ResultType.NEED_LOGIN, "请重新登陆");
        } catch (Exception e) {
            return new Result<>(ResultType.OTHER, "其他错误" + e.getMessage());
        }
    }

    public Result<List<Examination>> getStudentExamInformation(String year, String term) {
        if (lastLoginCookies == null) {
            return new Result<>(ResultType.NEED_LOGIN, "请先登陆");
        }
        try {
            Map<String, String> parameter = new HashMap<>(2);
            parameter.put("xnm", year);
            parameter.put("xqm", term);
            Connection connection = Jsoup.connect(getPrefix() + "/jwglxt/kwgl/kscx_cxXsksxxIndex.html?doType=query&gnmkdm=N358105");
            Connection.Response response = connection.cookies(lastLoginCookies).method(Connection.Method.POST)
                    .data(parameter).ignoreContentType(true).execute();
            JSONObject jsonObject = JSON.parseObject(response.body());
            JSONArray examTable = JSON.parseArray(jsonObject.getString("items"));
            List<Examination> examinationList = new ArrayList<>();
            for (Object o : examTable) {
                JSONObject exam = (JSONObject) o;
                Examination ans = JSON.parseObject(exam.toJSONString(), Examination.class);
                ans.setYear(year);
                ans.setTerm(term);
                examinationList.add(ans);
            }
            return new Result<>(ResultType.OK, "获取成功", examinationList);
        } catch (IOException e) {
            return new Result<>(ResultType.IO, "请检查网络连接");
        } catch (JSONException e) {
            return new Result<>(ResultType.NEED_LOGIN, "请重新登陆");
        } catch (Exception e) {
            return new Result<>(ResultType.OTHER, "其他错误");
        }

    }

    /**
     * 读取cookies
     */
    private void readCookies() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("cookies", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("cookies", "");
        @SuppressWarnings("unchecked")
        Map<String, String> map = JSON.parseObject(json, Map.class);
        lastLoginCookies = map;
    }

    /**
     * 保存cookies
     */
    public void saveCookies() {
        Object json = JSON.toJSON(lastLoginCookies);
        if (json != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("cookies", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("cookies", json.toString());
            editor.apply();
        }
    }

    private String getPrefix() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (sharedPreferences.getBoolean("useIntranet", false)) {
            return "http://www.gdjw.zjut.edu.cn";
        } else {
            //TODO 内网地址暂不知 回校测试
            return "http://www.gdjw.zjut.edu.cn";
        }
    }
}
