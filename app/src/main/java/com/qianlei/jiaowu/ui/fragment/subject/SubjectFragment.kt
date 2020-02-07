package com.qianlei.jiaowu.ui.fragment.subject

import android.content.Context
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
import androidx.preference.PreferenceManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.qianlei.jiaowu.MainApplication
import com.qianlei.jiaowu.R
import com.qianlei.jiaowu.common.Result
import com.qianlei.jiaowu.databinding.FragmentSubjectBinding
import com.qianlei.jiaowu.entity.Subject
import com.qianlei.jiaowu.ui.view.SubjectItemView
import com.qianlei.jiaowu.utils.TermUtil
import com.zhuangfei.timetable.model.Schedule
import com.zhuangfei.timetable.model.ScheduleSupport

/**
 * 课程显示的fragment
 *
 * @author qianlei
 */
class SubjectFragment : Fragment(), OnItemSelectedListener, OnRefreshListener {
    private lateinit var subjectViewModel: SubjectViewModel
    private lateinit var binding: FragmentSubjectBinding
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_subject, container, false)
        val factory = AndroidViewModelFactory(MainApplication.getInstance())
        subjectViewModel = factory.create(SubjectViewModel::class.java)
        subjectViewModel.subjectData.observe(this.viewLifecycleOwner, Observer { result: Result<List<Subject>> -> updateSubject(result) })
        binding.swipeRefreshLayout.setOnRefreshListener(this)
        binding.subjectTermChooseView.setItemSelectedListener(this)
        binding.weekView.curWeek(getStartTime()).hideLeftLayout().callback { week: Int -> changeWeek(week) }.showView()
        binding.timeTableView.curWeek(getStartTime()).isShowFlaglayout(false)
                .callback { _: View?, scheduleList: List<Schedule>? -> showSubjectItemDialog(scheduleList) }
                .showView()
        binding.lifecycleOwner = this
        return binding.root
    }

    /**
     * 显示课程具体弹窗
     *
     * @param scheduleList 点击的课程
     */
    private fun showSubjectItemDialog(scheduleList: List<Schedule>?) {
        if (scheduleList == null || scheduleList.isEmpty()) {
            return
        }
        val c = context
        if (c != null) {
            val dialog = BottomSheetDialog(c)
            val view: View = SubjectItemView(c, scheduleList[0])
            dialog.setContentView(view)
            dialog.show()
        }
    }

    /**
     * 修改课程
     *
     * @param result 课程内容
     */
    private fun updateSubject(result: Result<List<Subject>>) {
        if (result.data == null) {
            result.data = ArrayList()
        }
        binding.timeTableView.source(result.data).showView()
        binding.weekView.source(result.data).showView()
        if (!result.isSuccess()) {
            Toast.makeText(context, result.msg, Toast.LENGTH_SHORT).show()
        }
        binding.swipeRefreshLayout.isRefreshing = false
    }

    /**
     * 修改当前周
     *
     * @param week 修改后的周
     */
    private fun changeWeek(week: Int) {
        val cur = binding.timeTableView.curWeek()
        binding.timeTableView.onDateBuildListener().onUpdateDate(cur, week)
        binding.timeTableView.changeWeekOnly(week)
    }

    /**
     * 从设置中获取开始时间
     *
     * @return 开始时间
     */
    private fun getStartTime(): Int {
        val startTime: Int
        val context: Context = MainApplication.getInstance()
        val setStartTime = PreferenceManager.getDefaultSharedPreferences(context).getString("start_day", "2020年2月16日")
        startTime = ScheduleSupport.timeTransfrom("$setStartTime 00:00:00")
        return startTime
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        subjectViewModel.changeTerm(binding.subjectTermChooseView.term)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        subjectViewModel.changeTerm(TermUtil.getNowTerm())
    }

    override fun onRefresh() {
        subjectViewModel.refreshData(binding.subjectTermChooseView.term)
    }
}