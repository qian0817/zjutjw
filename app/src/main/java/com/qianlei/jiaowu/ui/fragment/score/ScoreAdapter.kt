package com.qianlei.jiaowu.ui.fragment.score

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.qianlei.jiaowu.R
import com.qianlei.jiaowu.entity.Score
import com.qianlei.jiaowu.ui.fragment.score.ScoreAdapter.ScoreViewHolder
import com.qianlei.jiaowu.utils.ScoreUtil

/**
 * 成绩适配器
 *
 * @author qianlei
 */
class ScoreAdapter internal constructor(private val scoreList: List<Score>?) : RecyclerView.Adapter<ScoreViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.score_item, parent, false)
        return ScoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        if (scoreList == null) {
            return
        }
        if (position == 0) { //第一个显示GPA信息
            holder.scoreText.text = ScoreUtil.getGpa(scoreList)
            holder.scoreNameText.setText(R.string.gpa)
            return
        }
        holder.scoreText.text = scoreList[position - 1].score
        holder.scoreNameText.text = scoreList[position - 1].name
    }

    override fun getItemCount(): Int {
        if (scoreList == null) {
            return 1
        }
        return scoreList.size + 1
    }

    class ScoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var scoreNameText: TextView = itemView.findViewById(R.id.score_name)
        var scoreText: TextView = itemView.findViewById(R.id.score_score)
    }

}