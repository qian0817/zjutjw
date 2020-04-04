package com.qianlei.jiaowu.ui.fragment.setting

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.afollestad.date.dayOfMonth
import com.afollestad.date.month
import com.afollestad.date.year
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.qianlei.jiaowu.R
import com.qianlei.jiaowu.db.MyDataBase
import com.qianlei.jiaowu.repository.HitokotoRepository
import com.qianlei.jiaowu.repository.SettingRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.*


/**
 * @author qianlei
 */
class SettingFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragement_setting, rootKey)
        //选择开学日期
        val startDayPreference = findPreference<Preference>(getString(R.string.start_day))
        if (startDayPreference != null) {
            setStartDaySummary()
            startDayPreference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                return@OnPreferenceClickListener changeStartDay()
            }
        }
        //清楚缓存
        val clearCachePreference = findPreference<Preference>(getString(R.string.clear_log))
        if (clearCachePreference != null) {
            clearCachePreference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                return@OnPreferenceClickListener deleteCache()
            }
        }
        val hitokotoPreference = findPreference<Preference>(getString(R.string.hitokoto))
        if (hitokotoPreference != null) {
            hitokotoPreference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
                return@OnPreferenceChangeListener changeHitokoto(newValue)
            }
        }
    }

    private fun changeHitokoto(newValue: Any?): Boolean {
        if (newValue is Boolean) {
            if (newValue) {
                HitokotoRepository.getPoem()
            } else {
                HitokotoRepository.hitokotoLiveData.value = ""
            }
        }
        return true
    }

    /**
     * 设置开始时间的简介部分
     */
    private fun setStartDaySummary() {
        val c = context ?: return
        val startDayPreference = findPreference<Preference>(getString(R.string.start_day)) ?: return
        startDayPreference.summary = SettingRepository.getStartDayFormat(c)
    }

    /**
     * 修改开始时间
     */
    private fun changeStartDay(): Boolean {
        val c = context ?: return false
        val date = SettingRepository.getStartDay(c)
        val calendar = Calendar.getInstance()
        calendar.time = date

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val datePickerDialog = DatePickerDialog(c)
            datePickerDialog.updateDate(calendar.year, calendar.month, calendar.dayOfMonth)
            datePickerDialog.setOnDateSetListener { _: DatePicker, year: Int, month: Int, day: Int ->
                SettingRepository.setStartDay(c, year, month, day)
                setStartDaySummary()
            }
            datePickerDialog.show()
        } else {
            MaterialDialog(c).show {
                datePicker(currentDate = calendar) { _, date ->
                    SettingRepository.setStartDay(c, date.year, date.month, date.dayOfMonth)
                    setStartDaySummary()
                }
            }
        }

        return true
    }

    /**
     * 删除缓存
     */
    private fun deleteCache(): Boolean {
        val c = context ?: return false
        Toast.makeText(c, "清除缓存成功", Toast.LENGTH_SHORT).show()
        GlobalScope.launch {
            //删除错误记录文件
            val file: File? = c.externalCacheDir
            if (file != null && file.exists() && file.isDirectory) {
                val files = file.listFiles()
                if (files != null) {
                    for (f in files) {
                        f.delete()
                    }
                }
            }
            //从数据库中删除所有的数据
            val dataBase: MyDataBase = MyDataBase.getDatabase(context)
            dataBase.examDao().deleteAll()
            dataBase.scoreDao().deleteAll()
            dataBase.subjectDao().deleteAll()
        }
        return true
    }
}