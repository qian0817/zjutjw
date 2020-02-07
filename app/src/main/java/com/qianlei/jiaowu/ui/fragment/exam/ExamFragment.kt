package com.qianlei.jiaowu.ui.fragment.exam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.qianlei.jiaowu.MainApplication
import com.qianlei.jiaowu.R
import com.qianlei.jiaowu.common.Result
import com.qianlei.jiaowu.databinding.FragmentExamBinding
import com.qianlei.jiaowu.entity.Examination
import com.qianlei.jiaowu.utils.TermUtil

/**
 * 考试的fragment
 *
 * @author qianlei
 */
class ExamFragment : Fragment(), OnItemSelectedListener, OnRefreshListener {
    private lateinit var examViewModel: ExamViewModel
    private lateinit var binding: FragmentExamBinding
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_exam, container, false)
        val factory = AndroidViewModelFactory(MainApplication.getInstance())
        examViewModel = factory.create(ExamViewModel::class.java)
        binding.termChooseView.setItemSelectedListener(this)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.swipeRefreshLayout.setOnRefreshListener(this)
        examViewModel.examData.observe(this.viewLifecycleOwner, Observer { result: Result<List<Examination>> -> updateExam(result) })
        binding.lifecycleOwner = this
        return binding.root
    }

    /**
     * 更新考试信息
     *
     * @param result 考试信息内容
     */
    private fun updateExam(result: Result<List<Examination>>) {
        //设置数据
        val data = result.data
        val adapter = if (data != null) {
            ExamAdapter(MainApplication.getInstance(), data)
        } else {
            ExamAdapter(MainApplication.getInstance(), ArrayList())
        }
        binding.recyclerView.adapter = adapter
        //如果不成功则弹出相关提示
        if (!result.isSuccess()) {
            Toast.makeText(context, result.msg, Toast.LENGTH_SHORT).show()
        }
        binding.swipeRefreshLayout.isRefreshing = false
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        examViewModel.changeTerm(binding.termChooseView.term)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        examViewModel.changeTerm(TermUtil.getNowTerm())
    }

    override fun onRefresh() {
        examViewModel.refreshData(binding.termChooseView.term)
    }
}