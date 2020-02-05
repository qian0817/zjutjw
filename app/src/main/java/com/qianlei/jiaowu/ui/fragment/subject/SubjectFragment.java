package com.qianlei.jiaowu.ui.fragment.subject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.qianlei.jiaowu.MainApplication;
import com.qianlei.jiaowu.R;
import com.qianlei.jiaowu.common.Result;
import com.qianlei.jiaowu.common.ResultType;
import com.qianlei.jiaowu.databinding.FragmentSubjectBinding;
import com.qianlei.jiaowu.entity.Subject;
import com.qianlei.jiaowu.utils.DateUtil;
import com.zhuangfei.timetable.model.ScheduleSupport;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 课程显示的fragment
 *
 * @author qianlei
 */
public class SubjectFragment extends Fragment implements AdapterView.OnItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {
    private SubjectViewModel subjectViewModel;

    private FragmentSubjectBinding binding;

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_subject, container, false);

        ViewModelProvider.AndroidViewModelFactory factory = new ViewModelProvider.AndroidViewModelFactory(MainApplication.getInstance());
        subjectViewModel = factory.create(SubjectViewModel.class);
        subjectViewModel.getResult().observe(this.getViewLifecycleOwner(), this::updateSubject);

        binding.swipeRefreshLayout.setOnRefreshListener(this);
        binding.subjectTermChooseView.setItemSelectedListener(this);
        binding.weekView.curWeek(getStartTime()).callback(this::changeWeek).showView();
        binding.timeTableView.curWeek(getStartTime()).isShowFlaglayout(false).showView();

        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    /**
     * 修改课程
     *
     * @param result 课程内容
     */
    private void updateSubject(@NotNull Result<List<Subject>> result) {
        if (result.getType() == ResultType.OK) {
            binding.timeTableView.source(result.getData()).showView();
            binding.weekView.source(result.getData()).showView();
        } else {
            Toast.makeText(getContext(), result.getMsg(), Toast.LENGTH_SHORT).show();
        }
        binding.swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * 修改当前周
     *
     * @param week 修改后的周
     */
    private void changeWeek(int week) {
        int cur = binding.timeTableView.curWeek();
        binding.timeTableView.onDateBuildListener().onUpdateDate(cur, week);
        binding.timeTableView.changeWeekOnly(week);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        subjectViewModel.changeTerm(binding.subjectTermChooseView.getYear(), binding.subjectTermChooseView.getTerm());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        subjectViewModel.changeTerm(String.valueOf(DateUtil.getCurYear()), String.valueOf(DateUtil.getCurTerm()));
    }

    @Override
    public void onRefresh() {
        subjectViewModel.refreshData(binding.subjectTermChooseView.getYear(), binding.subjectTermChooseView.getTerm());
    }
}