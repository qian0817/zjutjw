package com.qianlei.jiaowu.ui.fragment.exam;

import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qianlei.jiaowu.MainApplication;
import com.qianlei.jiaowu.R;
import com.qianlei.jiaowu.entity.Examination;

import java.util.Calendar;
import java.util.List;

/**
 * Exam适配器
 *
 * @author qianlei
 */
public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ExamViewHolder> {
    private List<Examination> examinationList;
    private Context context;

    ExamAdapter(Context context, List<Examination> examinationList) {
        this.examinationList = examinationList;
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    public ExamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exam_item, parent, false);

        return new ExamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamViewHolder holder, int position) {
        holder.examNameView.setText(examinationList.get(position).getName());
        holder.examTimeView.setText(examinationList.get(position).getTime());
        holder.examPlaceView.setText(examinationList.get(position).getPlace());
        holder.examSeatIdView.setText(examinationList.get(position).getSeatId());
        holder.addCalendarImageView.setOnClickListener(v -> {
            try {
                String time = examinationList.get(position).getTime();
                int year = Integer.valueOf(time.substring(0, 4));
                int month = Integer.valueOf(time.substring(5, 7)) - 1;
                int day = Integer.valueOf(time.substring(8, 10));
                Intent calendarIntent = new Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI);
                //开始时间
                int hour = Integer.valueOf(time.substring(11, 13));
                int minute = Integer.valueOf(time.substring(14, 16));
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day, hour, minute);
                calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calendar.getTimeInMillis());
                //结束时间
                hour = Integer.valueOf(time.substring(17, 19));
                minute = Integer.valueOf(time.substring(20, 22));
                calendar.set(year, month, day, hour, minute);
                calendarIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, calendar.getTimeInMillis());
                calendarIntent.putExtra(CalendarContract.Events.TITLE, examinationList.get(position).getName() + "考试");
                calendarIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, examinationList.get(position).getPlace());
                calendarIntent.putExtra(CalendarContract.Events.DESCRIPTION, "座位号" + examinationList.get(position).getSeatId());
                calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, CalendarContract.EXTRA_EVENT_ALL_DAY);
                //启动日历
                context.startActivity(calendarIntent);
            } catch (Exception e) {
                Toast.makeText(MainApplication.getInstance(), "添加到日历失败", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public int getItemCount() {
        return examinationList.size();
    }

    static class ExamViewHolder extends RecyclerView.ViewHolder {

        TextView examPlaceView;
        TextView examNameView;
        TextView examTimeView;
        TextView examSeatIdView;
        ImageView addCalendarImageView;

        ExamViewHolder(@NonNull View itemView) {
            super(itemView);
            examNameView = itemView.findViewById(R.id.exam_name_text_view);
            examPlaceView = itemView.findViewById(R.id.exam_place_text_view);
            examTimeView = itemView.findViewById(R.id.exam_time_text_view);
            examSeatIdView = itemView.findViewById(R.id.exam_seat_id_text_view);
            addCalendarImageView = itemView.findViewById(R.id.add_calendar_image_view);
        }
    }
}
