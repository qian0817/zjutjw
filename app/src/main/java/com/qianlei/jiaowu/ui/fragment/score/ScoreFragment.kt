package com.qianlei.jiaowu.ui.fragment.score

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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.qianlei.jiaowu.MainApplication
import com.qianlei.jiaowu.R
import com.qianlei.jiaowu.common.Result
import com.qianlei.jiaowu.common.ResultType
import com.qianlei.jiaowu.databinding.FragmentScoreBinding
import com.qianlei.jiaowu.entity.Score
import com.qianlei.jiaowu.utils.DateUtil

/**
 * 显示成绩的fragment
 *
 * @author qianlei
 */
class ScoreFragment : Fragment(), OnItemSelectedListener, OnRefreshListener {
    private lateinit var scoreViewModel: ScoreViewModel
    private lateinit var binding: FragmentScoreBinding
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_score, container, false)
        binding.swipeRefreshLayout.setOnRefreshListener(this)
        binding.scoreTermChooseView.setItemSelectedListener(this)
        binding.recycleScore.layoutManager = LinearLayoutManager(context)
        binding.recycleScore.addItemDecoration(DividerItemDecoration(MainApplication.getInstance(), DividerItemDecoration.VERTICAL))
        val factory = AndroidViewModelFactory(MainApplication.getInstance())
        scoreViewModel = factory.create(ScoreViewModel::class.java)
        scoreViewModel.result.observe(this.viewLifecycleOwner, Observer { result: Result<List<Score>> -> updateScore(result) })
        binding.lifecycleOwner = this
        return binding.root
    }

    /**
     * 更新成绩数据
     *
     * @param result 更新后的成绩数据
     */
    private fun updateScore(result: Result<List<Score>>) {
        if (result.type == ResultType.OK) {
            val data = result.data
            val adapter = ScoreAdapter(data)
            binding.recycleScore.adapter = adapter
        } else {
            Toast.makeText(context, result.msg, Toast.LENGTH_SHORT).show()
        }
        binding.swipeRefreshLayout.isRefreshing = false
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        scoreViewModel.changeTerm(binding.scoreTermChooseView.year, binding.scoreTermChooseView.term)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        scoreViewModel.changeTerm(DateUtil.getCurYear().toString(), DateUtil.getCurTerm().toString())
    }

    override fun onRefresh() {
        scoreViewModel.refreshData(binding.scoreTermChooseView.year, binding.scoreTermChooseView.term)
    }
}