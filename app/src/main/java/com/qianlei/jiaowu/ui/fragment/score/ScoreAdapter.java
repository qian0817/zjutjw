package com.qianlei.jiaowu.ui.fragment.score;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qianlei.jiaowu.R;
import com.qianlei.jiaowu.entity.Score;

import java.util.List;

/**
 * 成绩适配器
 *
 * @author qianlei
 */
public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder> {
    private List<Score> scoreList;

    ScoreAdapter(List<Score> scoreList) {
        this.scoreList = scoreList;
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_item, parent, false);

        return new ScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        holder.scoreText.setText(scoreList.get(position).getScore());
        holder.scoreNameText.setText(scoreList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return scoreList.size();
    }

    static class ScoreViewHolder extends RecyclerView.ViewHolder {
        TextView scoreNameText;
        TextView scoreText;

        ScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            scoreNameText = itemView.findViewById(R.id.score_name);
            scoreText = itemView.findViewById(R.id.score_score);
        }

    }
}