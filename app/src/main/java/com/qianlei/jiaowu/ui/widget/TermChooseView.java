package com.qianlei.jiaowu.ui.widget;

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
        return terms[termSpinner.getSelectedItemPosition()];
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
}
