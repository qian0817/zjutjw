package com.qianlei.jiaowu.ui.fragment.score

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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.qianlei.jiaowu.R
import com.qianlei.jiaowu.common.Result
import com.qianlei.jiaowu.entity.Score
import com.qianlei.jiaowu.utils.TermUtil
import kotlinx.android.synthetic.main.fragment_exam.swipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_score.*

/**
 * 显示成绩的fragment
 *
 * @author qianlei
 */
class ScoreFragment : Fragment(), OnItemSelectedListener, OnRefreshListener {
    private  val scoreViewModel by viewModels<ScoreViewModel>()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_score, container, false)
        scoreViewModel.scoreData.observe(this.viewLifecycleOwner, Observer { result: Result<List<Score>> -> updateScore(result) })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycleView.layoutManager = LinearLayoutManager(context)
        recycleView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary)
        scoreTermChooseView.setItemSelectedListener(this)
    }



    /**
     * 更新成绩数据
     *
     * @param result 更新后的成绩数据
     */
    private fun updateScore(result: Result<List<Score>>) {
        if (result.isSuccess()) {
            val data = result.data
            val adapter = ScoreAdapter(data)
            recycleView.adapter = adapter
        } else {
            Toast.makeText(context, result.msg, Toast.LENGTH_SHORT).show()
        }
        swipeRefreshLayout.isRefreshing = false
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        swipeRefreshLayout.isRefreshing = true
        scoreViewModel.changeTerm(scoreTermChooseView.term)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        swipeRefreshLayout.isRefreshing = true
        scoreViewModel.changeTerm(TermUtil.getNowTerm())
    }

    override fun onRefresh() {
        scoreViewModel.refreshData(scoreTermChooseView.term)
    }
}