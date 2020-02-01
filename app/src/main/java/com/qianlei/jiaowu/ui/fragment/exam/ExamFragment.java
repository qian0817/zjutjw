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

import com.qianlei.jiaowu.R;
import com.qianlei.jiaowu.ui.widget.TermChooseView;

/**
 * 考试的fragment
 *
 * @author qianlei
 */
public class ExamFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private ExamViewModel examViewModel;

    private TermChooseView termChooseView;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        examViewModel = ViewModelProviders.of(this).get(ExamViewModel.class);
        View root = inflater.inflate(R.layout.fragment_exam, container, false);

        init(root);
        examViewModel = ViewModelProviders.of(this).get(ExamViewModel.class);
        termChooseView.setItemSelectedListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        examViewModel.getExamListData().observe(this, result -> {
            if (result.isSuccess()) {
                ExamAdapter adapter = new ExamAdapter(result.getData());
                recyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(getContext(), result.getMsg(), Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

    private void init(View root) {
        termChooseView = root.findViewById(R.id.term_choose_view);
        recyclerView = root.findViewById(R.id.recycler_view);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        examViewModel.changeTerm(termChooseView.getYear(), termChooseView.getTerm());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}