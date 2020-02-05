package com.qianlei.jiaowu.ui.fragment.score;

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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.qianlei.jiaowu.MainApplication;
import com.qianlei.jiaowu.R;
import com.qianlei.jiaowu.common.Result;
import com.qianlei.jiaowu.common.ResultType;
import com.qianlei.jiaowu.databinding.FragmentScoreBinding;
import com.qianlei.jiaowu.entity.Score;
import com.qianlei.jiaowu.utils.DateUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;

/**
 * 显示成绩的fragment
 *
 * @author qianlei
 */
public class ScoreFragment extends Fragment implements AdapterView.OnItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    private ScoreViewModel scoreViewModel;
    private FragmentScoreBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_score, container, false);

        binding.swipeRefreshLayout.setOnRefreshListener(this);
        binding.scoreTermChooseView.setItemSelectedListener(this);
        binding.recycleScore.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recycleScore.addItemDecoration(new DividerItemDecoration(MainApplication.getInstance(), VERTICAL));
        ViewModelProvider.AndroidViewModelFactory factory = new ViewModelProvider.AndroidViewModelFactory(MainApplication.getInstance());
        scoreViewModel = factory.create(ScoreViewModel.class);
        scoreViewModel.getResult().observe(this.getViewLifecycleOwner(), this::updateScore);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    /**
     * 更新成绩数据
     *
     * @param result 更新后的成绩数据
     */
    private void updateScore(@NotNull Result<List<Score>> result) {
        if (result.getType() == ResultType.OK) {
            ScoreAdapter adapter = new ScoreAdapter(result.getData());
            binding.recycleScore.setAdapter(adapter);
        } else {
            Toast.makeText(getContext(), result.getMsg(), Toast.LENGTH_SHORT).show();
        }
        binding.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        scoreViewModel.changeTerm(binding.scoreTermChooseView.getYear(), binding.scoreTermChooseView.getTerm());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        scoreViewModel.changeTerm(String.valueOf(DateUtil.getCurYear()), String.valueOf(DateUtil.getCurTerm()));
    }

    @Override
    public void onRefresh() {
        scoreViewModel.refreshData(binding.scoreTermChooseView.getYear(), binding.scoreTermChooseView.getTerm());
    }
}

