package com.qianlei.jiaowu.ui.fragment.subject

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.preference.PreferenceManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.qianlei.jiaowu.R
import com.qianlei.jiaowu.common.Result
import com.qianlei.jiaowu.entity.Subject
import com.qianlei.jiaowu.repository.SettingRepository
import com.qianlei.jiaowu.ui.view.SubjectItemView
import com.qianlei.jiaowu.utils.TermUtil
import com.zhuangfei.timetable.model.Schedule
import com.zhuangfei.timetable.model.ScheduleSupport
import kotlinx.android.synthetic.main.fragment_subject.*


/**
 * 课程显示的fragment
 *
 * @author qianlei
 */
class SubjectFragment : Fragment(), OnItemSelectedListener, OnRefreshListener {
    private lateinit var subjectViewModel: SubjectViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_subject, container, false)
        val factory = AndroidViewModelFactory(activity!!.application)
        subjectViewModel = factory.create(SubjectViewModel::class.java)
        subjectViewModel.subjectData.observe(this.viewLifecycleOwner, Observer { result: Result<List<Subject>> -> updateSubject(result) })
        return root
    }

    @SuppressLint("InflateParams")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subjectTermChooseView.setItemSelectedListener(this)
        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary)
        weekView.curWeek(getStartTime()).itemCount(16)
                .hideLeftLayout().callback { week: Int -> changeWeek(week) }.showView()
        //是否显示周末
        val preferenceManager = PreferenceManager.getDefaultSharedPreferences(context)
        val showWeek = preferenceManager.getBoolean(getString(R.string.show_weekend), true)
        timeTableView.curWeek(getStartTime()).isShowFlaglayout(false)
                .isShowWeekends(showWeek)
                .callback { mInflate: LayoutInflater -> mInflate.inflate(R.layout.custom_myscrollview, null, false) }
                .callback { _: View?, scheduleList: List<Schedule>? -> showSubjectItemDialog(scheduleList) }
                .showView()
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
        val c = context ?: return
        val dialog = BottomSheetDialog(c)
        val view: View = SubjectItemView(c, scheduleList[0])
        dialog.setContentView(view)
        dialog.show()
    }


    /**
     * 修改课程
     *
     * @param result 课程内容
     */
    private fun updateSubject(result: Result<List<Subject>>) {
        if (result.isSuccess()) {
            timeTableView.source(result.data).showView()
            weekView.source(result.data).showView()
        } else {
            Toast.makeText(context, result.msg, Toast.LENGTH_SHORT).show()
        }
        swipeRefreshLayout.isRefreshing = false
    }

    /**
     * 修改当前周
     *
     * @param week 修改后的周
     */
    private fun changeWeek(week: Int) {
        val cur = timeTableView.curWeek()
        timeTableView.onDateBuildListener().onUpdateDate(cur, week)
        timeTableView.changeWeekOnly(week)
    }

    /**
     * 从设置中获取开始时间
     *
     * @return 开始时间
     */
    private fun getStartTime(): Int {
        val c = context ?: return 1
        val setStartTime = SettingRepository.getInstance(c).getStartDayFormat()
        return ScheduleSupport.timeTransfrom("$setStartTime 00:00:00")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        swipeRefreshLayout.isRefreshing = true
        subjectViewModel.changeTerm(subjectTermChooseView.term)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        swipeRefreshLayout.isRefreshing = true
        subjectViewModel.changeTerm(TermUtil.getNowTerm())
    }

    override fun onRefresh() {
        subjectViewModel.refreshData(subjectTermChooseView.term)
    }
}