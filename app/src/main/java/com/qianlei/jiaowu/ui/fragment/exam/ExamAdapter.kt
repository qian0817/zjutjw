package com.qianlei.jiaowu.ui.fragment.exam

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.qianlei.jiaowu.R
import com.qianlei.jiaowu.entity.Examination
import com.qianlei.jiaowu.ui.fragment.exam.ExamAdapter.ExamViewHolder
import java.util.*

/**
 * Exam适配器
 *
 * @author qianlei
 */
class ExamAdapter(
    context: Context,
    private val examinationList: List<Examination>?
) : RecyclerView.Adapter<ExamViewHolder>() {

    private val context: Context = context.applicationContext

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.exam_item, parent, false)
        return ExamViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExamViewHolder, position: Int) {
        if (examinationList == null) {
            return
        }
        holder.examNameView.text = examinationList[position].name
        holder.examTimeView.text = examinationList[position].time
        holder.examPlaceView.text = examinationList[position].place
        holder.examSeatIdView.text = examinationList[position].seatId
        holder.addCalendarImageView.setOnClickListener { startCalender(position) }
    }

    private fun startCalender(position: Int) {
        if (examinationList == null) {
            return
        }
        try {
            examinationList[position]
            val time = examinationList[position].time
            if (time != null) {
                val year = Integer.valueOf(time.substring(0, 4))
                val month = Integer.valueOf(time.substring(5, 7)) - 1
                val day = Integer.valueOf(time.substring(8, 10))
                val calendarIntent = Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI)
                //开始时间
                var hour = Integer.valueOf(time.substring(11, 13))
                var minute = Integer.valueOf(time.substring(14, 16))
                val calendar = Calendar.getInstance()
                calendar[year, month, day, hour] = minute
                calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calendar.timeInMillis)
                //结束时间
                hour = Integer.valueOf(time.substring(17, 19))
                minute = Integer.valueOf(time.substring(20, 22))
                calendar[year, month, day, hour] = minute
                calendarIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, calendar.timeInMillis)
                calendarIntent.putExtra(CalendarContract.Events.TITLE, examinationList[position].name + "考试")
                calendarIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, examinationList[position].place)
                calendarIntent.putExtra(CalendarContract.Events.DESCRIPTION, "座位号" + examinationList[position].seatId)
                calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, CalendarContract.EXTRA_EVENT_ALL_DAY)
                //启动日历
                context.startActivity(calendarIntent)
            }
        } catch (e: Exception) {
            Toast.makeText(context, "添加到日历失败", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        if (examinationList == null) {
            return 0
        }
        return examinationList.size
    }

    class ExamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var examPlaceView: TextView = itemView.findViewById(R.id.exam_place_text_view)
        var examNameView: TextView = itemView.findViewById(R.id.exam_name_text_view)
        var examTimeView: TextView = itemView.findViewById(R.id.exam_time_text_view)
        var examSeatIdView: TextView = itemView.findViewById(R.id.exam_seat_id_text_view)
        var addCalendarImageView: ImageView = itemView.findViewById(R.id.add_calendar_image_view)
    }

}