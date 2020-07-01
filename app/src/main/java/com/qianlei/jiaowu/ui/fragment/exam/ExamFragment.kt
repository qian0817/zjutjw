package com.qianlei.jiaowu.ui.fragment.exam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.qianlei.jiaowu.R
import com.qianlei.jiaowu.common.Result
import com.qianlei.jiaowu.entity.Examination
import com.qianlei.jiaowu.entity.Term
import kotlinx.android.synthetic.main.fragment_exam.*

/**
 * 考试的fragment
 *
 * @author qianlei
 */
class ExamFragment : Fragment(), OnItemSelectedListener, OnRefreshListener {
    private val examViewModel by viewModels<ExamViewModel>()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_exam, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        termChooseView.setItemSelectedListener(this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary)
        examViewModel.examData.observe(this.viewLifecycleOwner, Observer { result: Result<List<Examination>> -> updateExam(result) })
    }


    /**
     * 更新考试信息
     *
     * @param result 考试信息内容
     */
    private fun updateExam(result: Result<List<Examination>>) {
        val c = context ?: return
        if (result.isSuccess()) {
            val data = result.data
            val adapter = ExamAdapter(c, data)
            recyclerView.adapter = adapter
        } else {
            Toast.makeText(context, result.msg, Toast.LENGTH_SHORT).show()
        }
        swipeRefreshLayout.isRefreshing = false
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        swipeRefreshLayout.isRefreshing = true
        examViewModel.changeTerm(termChooseView.term)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        swipeRefreshLayout.isRefreshing = true
        examViewModel.changeTerm(Term.getNowTerm())
    }

    override fun onRefresh() {
        examViewModel.refreshData(termChooseView.term)
    }
}