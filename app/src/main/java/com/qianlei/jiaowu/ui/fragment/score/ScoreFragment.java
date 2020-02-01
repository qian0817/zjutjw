package com.qianlei.jiaowu.ui.fragment.score;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.qianlei.jiaowu.R;
import com.qianlei.jiaowu.common.ResultType;
import com.qianlei.jiaowu.ui.widget.TermChooseView;
import com.qianlei.jiaowu.utils.DateUtil;

/**
 * 显示成绩的fragment
 *
 * @author qianlei
 */
public class ScoreFragment extends Fragment implements AdapterView.OnItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    private ScoreViewModel scoreViewModel;
    private RecyclerView recyclerView;
    private TermChooseView termChooseView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_score, container, false);

        init(root);

        swipeRefreshLayout.setOnRefreshListener(this);
        termChooseView.setItemSelectedListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        scoreViewModel = ViewModelProviders.of(this).get(ScoreViewModel.class);
        scoreViewModel.getResult().observe(this, result -> {
            if (result.getType() == ResultType.OK) {
                ScoreAdapter adapter = new ScoreAdapter(result.getData());
                recyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(getContext(), result.getMsg(), Toast.LENGTH_SHORT).show();
            }
            swipeRefreshLayout.setRefreshing(false);
        });
        return root;
    }

    private void init(View root) {
        recyclerView = root.findViewById(R.id.recycle_score);
        termChooseView = root.findViewById(R.id.score_term_choose_view);
        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh_layout);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        scoreViewModel.changeTerm(termChooseView.getYear(), termChooseView.getTerm());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        scoreViewModel.changeTerm(String.valueOf(DateUtil.getCurYear()), String.valueOf(DateUtil.getCurTerm()));
    }

    @Override
    public void onRefresh() {
        scoreViewModel.changeTerm(termChooseView.getYear(), termChooseView.getTerm());
    }
}

