package com.qianlei.jiaowu.ui.fragment.subject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.qianlei.jiaowu.MainApplication;
import com.qianlei.jiaowu.R;
import com.qianlei.jiaowu.common.ResultType;
import com.qianlei.jiaowu.ui.widget.TermChooseView;
import com.qianlei.jiaowu.utils.DateUtil;
import com.zhuangfei.timetable.TimetableView;
import com.zhuangfei.timetable.model.ScheduleSupport;
import com.zhuangfei.timetable.view.WeekView;

/**
 * 课程显示的fragment
 *
 * @author qianlei
 */
public class SubjectFragment extends Fragment implements AdapterView.OnItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {
    private SubjectViewModel subjectViewModel;

    private TermChooseView termChooseView;
    private TimetableView timetableView;
    private WeekView weekView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_subject, container, false);
        init(root);

        swipeRefreshLayout.setOnRefreshListener(this);
        termChooseView.setItemSelectedListener(this);
        //修改周数时的方法
        weekView.curWeek(getStartTime()).callback(week -> {
            int cur = timetableView.curWeek();
            //切换日期
            timetableView.onDateBuildListener().onUpdateDate(cur, week);
            timetableView.changeWeekOnly(week);
        }).showView();

        timetableView.curWeek(getStartTime())
                .isShowFlaglayout(false)
                .isShowNotCurWeek(false)
                .showView();
        subjectViewModel = ViewModelProviders.of(this).get(SubjectViewModel.class);
        subjectViewModel.getResult().observe(this, result -> {
            if (result.getType() == ResultType.OK) {
                timetableView.source(result.getData())
                        .curTerm(termChooseView.getTerm())
                        .showView();
                weekView.source(result.getData())
                        .showView();
            } else {
                Toast.makeText(root.getContext(), result.getMsg(), Toast.LENGTH_SHORT).show();
            }
            swipeRefreshLayout.setRefreshing(false);
        });
        return root;
    }

    /**
     * 从设置中获取开始时间
     *
     * @return 开始时间
     */
    private int getStartTime() {
        int startTime;
        Context context = MainApplication.getInstance();
        String setStartTime = PreferenceManager.getDefaultSharedPreferences(context).getString("start_day", "2020-2-16");
        startTime = ScheduleSupport.timeTransfrom(setStartTime + " 00:00:00");
        return startTime;
    }

    private void init(View root) {
        termChooseView = root.findViewById(R.id.subject_term_choose_view);
        timetableView = root.findViewById(R.id.time_table_view);
        weekView = root.findViewById(R.id.week_view);
        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh_layout);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        subjectViewModel.changeTerm(termChooseView.getYear(), termChooseView.getTerm());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        subjectViewModel.changeTerm(String.valueOf(DateUtil.getCurYear()), String.valueOf(DateUtil.getCurTerm()));
    }

    @Override
    public void onRefresh() {
        subjectViewModel.changeTerm(termChooseView.getYear(), termChooseView.getTerm());
    }
}