package com.qianlei.jiaowu.ui.fragment.exam;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qianlei.jiaowu.R;
import com.qianlei.jiaowu.entity.Examination;

import java.util.List;

/**
 * Exam适配器
 *
 * @author qianlei
 */
public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ExamViewHolder> {
    private List<Examination> examinationList;

    ExamAdapter(List<Examination> examinationList) {
        this.examinationList = examinationList;
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

        ExamViewHolder(@NonNull View itemView) {
            super(itemView);
            examNameView = itemView.findViewById(R.id.exam_name_text_view);
            examPlaceView = itemView.findViewById(R.id.exam_place_text_view);
            examTimeView = itemView.findViewById(R.id.exam_time_text_view);
            examSeatIdView = itemView.findViewById(R.id.exam_seat_id_text_view);
        }
    }
}
