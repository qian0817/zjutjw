package com.qianlei.jiaowu.ui.fragment.exam;

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

import com.qianlei.jiaowu.MainApplication;
import com.qianlei.jiaowu.R;
import com.qianlei.jiaowu.ui.widget.TermChooseView;
import com.qianlei.jiaowu.utils.DateUtil;

/**
 * 考试的fragment
 *
 * @author qianlei
 */
public class ExamFragment extends Fragment implements AdapterView.OnItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    private ExamViewModel examViewModel;

    private TermChooseView termChooseView;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        examViewModel = ViewModelProviders.of(this).get(ExamViewModel.class);
        View root = inflater.inflate(R.layout.fragment_exam, container, false);

        init(root);
        examViewModel = ViewModelProviders.of(this).get(ExamViewModel.class);
        termChooseView.setItemSelectedListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        swipeRefreshLayout.setOnRefreshListener(this);
        examViewModel.getExamListData().observe(this, result -> {
            if (result.isSuccess()) {
                ExamAdapter adapter = new ExamAdapter(MainApplication.getInstance(), result.getData());
                recyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(getContext(), result.getMsg(), Toast.LENGTH_SHORT).show();
            }
            swipeRefreshLayout.setRefreshing(false);
        });
        return root;
    }

    private void init(View root) {
        termChooseView = root.findViewById(R.id.term_choose_view);
        recyclerView = root.findViewById(R.id.recycler_view);
        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        examViewModel.changeTerm(termChooseView.getYear(), termChooseView.getTerm());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        examViewModel.changeTerm(String.valueOf(DateUtil.getCurYear()), String.valueOf(termChooseView.getTerm()));
    }

    @Override
    public void onRefresh() {
        examViewModel.refreshData(termChooseView.getYear(), termChooseView.getTerm());
    }
}