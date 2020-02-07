package com.qianlei.jiaowu.ui.fragment.setting

import android.os.Bundle
import android.widget.Toast
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.qianlei.jiaowu.MainApplication
import com.qianlei.jiaowu.R
import com.qianlei.jiaowu.db.MyDataBase
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author qianlei
 */
class SettingFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragement_setting, rootKey)
        val editTextPreference = findPreference<EditTextPreference>("start_day")
        if (editTextPreference != null) {
            editTextPreference.summary = editTextPreference.text
            //对输入的进行检测
            editTextPreference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _: Preference?, newValue: Any? ->
                if (newValue is String) {
                    val dateFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM, Locale.CHINA)
                    try {
                        dateFormat.parse(newValue)
                        editTextPreference.summary = newValue
                        return@OnPreferenceChangeListener true
                    } catch (e: ParseException) {
                        Toast.makeText(context, "日期填写错误,格式为yyyy年MM月dd日", Toast.LENGTH_SHORT).show()
                        return@OnPreferenceChangeListener false
                    }
                }
                return@OnPreferenceChangeListener true
            }
        }
        val preference = findPreference<Preference>("clear_log")
        if (preference != null) {
            //删除缓存文件
            preference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                Toast.makeText(MainApplication.getInstance(), "清除缓存成功", Toast.LENGTH_SHORT).show()
                Thread(Runnable {
                    //删除错误记录文件
                    val file: File? = MainApplication.getInstance().externalCacheDir
                    if (file != null && file.exists() && file.isDirectory) {
                        val files = file.listFiles()
                        if (files != null) {
                            for (f in files) {
                                f.delete()
                            }
                        }
                    }
                    //从数据库中删除所有的数据
                    val dataBase: MyDataBase = MyDataBase.getDatabase(MainApplication.getInstance())
                    dataBase.examDao().deleteAll()
                    dataBase.scoreDao().deleteAll()
                    dataBase.subjectDao().deleteAll()
                }).start()
                return@OnPreferenceClickListener true
            }
        }
    }
}