package com.qianlei.jiaowu.ui.fragment.setting;

import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.qianlei.jiaowu.MainApplication;
import com.qianlei.jiaowu.R;
import com.qianlei.jiaowu.core.db.MyDataBase;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;


/**
 * @author qianlei
 */
public class SettingFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.fragement_setting, rootKey);
        EditTextPreference editTextPreference = findPreference("start_day");
        if (editTextPreference != null) {
            editTextPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                if (newValue instanceof String) {
                    String s = (String) newValue;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                    try {
                        dateFormat.parse(s);
                        editTextPreference.setSummary(s);
                        return true;
                    } catch (ParseException e) {
                        Toast.makeText(getContext(), "日期填写错误,格式为yyyy-MM-dd", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
                return true;
            });
        }
        Preference preference = findPreference("clear_log");
        if (preference != null) {
            preference.setOnPreferenceClickListener(preference1 -> {
                Toast.makeText(MainApplication.getInstance(), "清除缓存成功", Toast.LENGTH_SHORT).show();
                new Thread(() -> {
                    //删除缓存文件
                    File file = MainApplication.getInstance().getExternalCacheDir();
                    if (file != null && file.exists() && file.isDirectory()) {
                        File[] files = file.listFiles();
                        if (files != null) {
                            for (File f : files) {
                                //noinspection ResultOfMethodCallIgnored
                                f.delete();
                            }
                        }
                    }
                    //从数据库中删除所有的数据
                    MyDataBase dataBase = MyDataBase.getDatabase(MainApplication.getInstance());
                    dataBase.getExamDao().deleteAll();
                    dataBase.getScoreDao().deleteAll();
                    dataBase.getSubjectDao().deleteAll();
                }).start();
                return true;
            });
        }


    }
}
