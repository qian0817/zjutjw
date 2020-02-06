package com.qianlei.jiaowu.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;

import com.qianlei.jiaowu.R;
import com.zhuangfei.timetable.model.Schedule;

import java.util.Locale;

/**
 * @author qianlei
 */
public class SubjectItemView extends NestedScrollView {
    private TextView teacherNameTextView;
    private TextView subjectNameTextView;
    private TextView subjectPlaceTextView;
    private TextView subjectLastTextView;
    private TextView subjectWeekTextView;

    public SubjectItemView(@NonNull Context context) {
        super(context);
    }

    public SubjectItemView(Context context, Schedule schedule) {
        this(context);
        LayoutInflater.from(context).inflate(R.layout.subject_item_dialog, this);

        findView();
        teacherNameTextView.setText(schedule.getTeacher());
        subjectNameTextView.setText(schedule.getName());
        subjectPlaceTextView.setText(schedule.getRoom());
        subjectLastTextView.setText(String.format(Locale.CHINA, "%d-%d", schedule.getStart(), schedule.getStart() + schedule.getStep() - 1));
        subjectWeekTextView.setText(schedule.getWeekList().toString());
    }

    private void findView() {
        teacherNameTextView = findViewById(R.id.teacher_name_text_view);
        subjectNameTextView = findViewById(R.id.subject_name_text_view);
        subjectPlaceTextView = findViewById(R.id.place_text_view);
        subjectLastTextView = findViewById(R.id.subject_last_text_view);
        subjectWeekTextView = findViewById(R.id.week_text_view);
    }

}
