package com.qianlei.jiaowu.ui.fragment.exam;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.qianlei.jiaowu.R;

/**
 * 考试的fragment
 *
 * @author qianlei
 */
public class ExamFragment extends Fragment {

    private ExamViewModel examViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        examViewModel = ViewModelProviders.of(this).get(ExamViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        examViewModel.getText().observe(this, textView::setText);
        return root;
    }
}