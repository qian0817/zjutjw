package com.qianlei.jiaowu.ui.fragment.subject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

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
public class SubjectFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private SubjectViewModel subjectViewModel;

    private View root;
    private TermChooseView termChooseView;
    private TimetableView timetableView;
    private WeekView weekView;

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_subject, container, false);
        this.root = root;
        init();

        subjectViewModel = ViewModelProviders.of(this).get(SubjectViewModel.class);
        //当ViewModel内的内容发生改变时，将内容更新
        subjectViewModel.getResult().observe(this, result -> {
            if (result.getType() == ResultType.OK) {
                timetableView.source(result.getData()).showView();
                weekView.source(result.getData()).showView();
            } else {
                Toast.makeText(root.getContext(), result.getMsg(), Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

    private void init() {
        termChooseView = root.findViewById(R.id.subject_term_choose_view);
        timetableView = root.findViewById(R.id.time_table_view);
        weekView = root.findViewById(R.id.week_view);
        int startTime = ScheduleSupport.timeTransfrom("2019-9-16 00:00:00");
        weekView.curWeek(startTime).callback(week -> {
            int cur = timetableView.curWeek();
            //切换日期
            timetableView.onDateBuildListener().onUpdateDate(cur, week);
            timetableView.changeWeekOnly(week);
        }).showView();
        timetableView.curWeek(startTime).curTerm("").isShowNotCurWeek(false).showView();
        termChooseView.setItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        subjectViewModel.changeTerm(termChooseView.getYear(), termChooseView.getTerm());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        subjectViewModel.changeTerm(String.valueOf(DateUtil.getCurYear()), String.valueOf(DateUtil.getCurTerm()));
    }
}