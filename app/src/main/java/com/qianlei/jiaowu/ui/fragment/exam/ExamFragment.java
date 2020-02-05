package com.qianlei.jiaowu.ui.fragment.exam;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.qianlei.jiaowu.MainApplication;
import com.qianlei.jiaowu.R;
import com.qianlei.jiaowu.common.Result;
import com.qianlei.jiaowu.databinding.FragmentExamBinding;
import com.qianlei.jiaowu.entity.Examination;
import com.qianlei.jiaowu.utils.DateUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 考试的fragment
 *
 * @author qianlei
 */
public class ExamFragment extends Fragment implements AdapterView.OnItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    private ExamViewModel examViewModel;
    private FragmentExamBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_exam, container, false);

        ViewModelProvider.AndroidViewModelFactory factory = new ViewModelProvider.AndroidViewModelFactory(MainApplication.getInstance());
        examViewModel = factory.create(ExamViewModel.class);
        binding.termChooseView.setItemSelectedListener(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.swipeRefreshLayout.setOnRefreshListener(this);
        examViewModel.getExamListData().observe(this.getViewLifecycleOwner(), this::updateExam);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    /**
     * 更新考试信息
     *
     * @param result 考试信息内容
     */
    private void updateExam(@NotNull Result<List<Examination>> result) {
        if (result.isSuccess()) {
            ExamAdapter adapter = new ExamAdapter(MainApplication.getInstance(), result.getData());
            binding.recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(getContext(), result.getMsg(), Toast.LENGTH_SHORT).show();
        }
        binding.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        examViewModel.changeTerm(binding.termChooseView.getYear(), binding.termChooseView.getTerm());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        examViewModel.changeTerm(String.valueOf(DateUtil.getCurYear()), String.valueOf(DateUtil.getCurTerm()));
    }

    @Override
    public void onRefresh() {
        examViewModel.refreshData(binding.termChooseView.getYear(), binding.termChooseView.getTerm());
    }
}