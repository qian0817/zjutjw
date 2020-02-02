package com.qianlei.jiaowu.ui.fragment.setting;

import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.qianlei.jiaowu.R;

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
    }
}
