package com.qianlei.jiaowu.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import com.qianlei.jiaowu.R;
import com.qianlei.jiaowu.utils.DateUtil;

/**
 * @author qianlei
 */
public class TermChooseView extends LinearLayout {
    private Spinner yearSpinner;
    private Spinner termSpinner;

    public TermChooseView(Context context) {
        this(context, null);
    }

    public TermChooseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.term_choose_view, this);
        yearSpinner = findViewById(R.id.spinner_year);
        termSpinner = findViewById(R.id.spinner_term);
        //设置选中项为当前学期
        yearSpinner.setSelection(DateUtil.getCurYear() - 2017);
        termSpinner.setSelection(DateUtil.getCurTerm() - 1);
    }

    public void setItemSelectedListener(AdapterView.OnItemSelectedListener listener) {
        yearSpinner.setOnItemSelectedListener(listener);
        termSpinner.setOnItemSelectedListener(listener);
    }

    /**
     * 获取选择的学期
     *
     * @return 当前用户选择的学期
     */
    public String getTerm() {
        String[] terms = getResources().getStringArray(R.array.term);
        String term = terms[termSpinner.getSelectedItemPosition()];
        return formatTerm(term);
    }

    /**
     * 获取选择的学年
     *
     * @return 当前用户选择的学年
     */
    public String getYear() {
        String[] years = getResources().getStringArray(R.array.year);
        return years[yearSpinner.getSelectedItemPosition()].substring(0, 4);
    }

    /**
     * 格式化term
     * 传到教务系统的学期格式需要转换
     *
     * @param term 转换前的学期
     * @return 转换后的学期
     */
    private String formatTerm(String term) {
        switch (term) {
            case "2":
                return "12";
            case "3":
                return "16";
            case "1":
            default:
                return "3";
        }
    }
}
