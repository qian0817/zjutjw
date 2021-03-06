package com.qianlei.jiaowu.ui.view

import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import com.qianlei.jiaowu.R
import com.zhuangfei.timetable.model.Schedule

/**
 * @author qianlei
 */
class SubjectItemView(context: Context) : NestedScrollView(context) {
    private lateinit var teacherNameTextView: TextView
    private lateinit var subjectNameTextView: TextView
    private lateinit var subjectPlaceTextView: TextView
    private lateinit var subjectLastTextView: TextView
    private lateinit var subjectWeekTextView: TextView

    constructor(context: Context, schedule: Schedule) : this(context) {
        LayoutInflater.from(context).inflate(R.layout.subject_item_dialog, this)
        findView()
        teacherNameTextView.text = schedule.teacher
        subjectNameTextView.text = schedule.name
        subjectPlaceTextView.text = schedule.room
        subjectLastTextView.text = schedule.extras["last"].toString()
        subjectWeekTextView.text = schedule.extras["week"].toString()
    }

    private fun findView() {
        teacherNameTextView = findViewById(R.id.teacher_name_text_view)
        subjectNameTextView = findViewById(R.id.subject_name_text_view)
        subjectPlaceTextView = findViewById(R.id.place_text_view)
        subjectLastTextView = findViewById(R.id.subject_last_text_view)
        subjectWeekTextView = findViewById(R.id.week_text_view)
    }
}