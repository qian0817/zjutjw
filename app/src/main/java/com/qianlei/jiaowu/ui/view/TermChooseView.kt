package com.qianlei.jiaowu.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.LinearLayout
import android.widget.Spinner
import com.qianlei.jiaowu.R
import com.qianlei.jiaowu.entity.Term

/**
 * @author qianlei
 */
class TermChooseView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {
    private val yearSpinner: Spinner
    private val termSpinner: Spinner
    fun setItemSelectedListener(listener: OnItemSelectedListener?) {
        yearSpinner.onItemSelectedListener = listener
        termSpinner.onItemSelectedListener = listener
    }

    /**
     * 获取选择的学期
     *
     * @return 当前用户选择的学期
     */
    val term: Term
        get() {
            val years = resources.getStringArray(R.array.year)
            val terms = resources.getStringArray(R.array.term)
            val term = terms[termSpinner.selectedItemPosition]
            val year = years[yearSpinner.selectedItemPosition].substring(0, 4)
            return Term(year, formatTerm(term))
        }

    /**
     * 格式化term
     * 传到教务系统的学期格式需要转换
     *
     * @param term 转换前的学期
     * @return 转换后的学期
     */
    private fun formatTerm(term: String): String {
        return when (term) {
            "2" -> "12"
            "3" -> "16"
            "1" -> "3"
            else -> "3"
        }
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.term_choose_view, this)
        yearSpinner = findViewById(R.id.spinner_year)
        termSpinner = findViewById(R.id.spinner_term)
        val years = resources.getStringArray(R.array.year)
        //设置选中项为当前学期
        yearSpinner.setSelection(Term.getNowTerm().year.toInt() - years[0].substring(0, 4).toInt())
        termSpinner.setSelection(Term.getNowTerm().term.toInt() - 1)
    }
}