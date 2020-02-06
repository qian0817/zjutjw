package com.qianlei.jiaowu.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alibaba.fastjson.annotation.JSONField
import com.zhuangfei.timetable.model.Schedule
import com.zhuangfei.timetable.model.ScheduleEnable
import java.util.*

/**
 * 课程的实体类
 *
 * @author qianlei
 */
@Entity
class Subject : ScheduleEnable {
    @PrimaryKey(autoGenerate = true)
    var id = 0
    /**
     * 课程名称
     */
    @ColumnInfo(name = "name")
    @JSONField(name = "kcmc")
    var name: String? = null
    /**
     * 任课教师
     */
    @ColumnInfo(name = "teacher")
    @JSONField(name = "xm")
    var teacher: String? = null
    /**
     * 上课地点
     */
    @ColumnInfo(name = "place")
    @JSONField(name = "cdmc")
    var place: String? = null
    /**
     * 上课星期
     * 具体的格式会有
     * 1-8
     */
    @ColumnInfo(name = "week")
    @JSONField(name = "zcd")
    var week: String? = null
    /**
     * 星期几
     * 格式为 星期X
     */
    @ColumnInfo(name = "day")
    @JSONField(name = "xqjmc")
    var day: String? = null
    /**
     * 上课时间
     * 格式为 X-X
     */
    @ColumnInfo(name = "last")
    @JSONField(name = "jc")
    var last: String? = null
    /**
     * 学年
     */
    @ColumnInfo(name = "year")
    @JSONField(name = "xnm")
    var year: String? = null
    /**
     * 学年
     */
    @ColumnInfo(name = "term")
    @JSONField(name = "xqm")
    var term: String? = null

    override fun getSchedule(): Schedule {
        week = week!!.replace("周", "")
        last = last!!.replace("节", "")
        val tempDay = formatDay()
        val list = formatWeek()
        val tmp = last!!.split("-").toTypedArray()
        val start = Integer.valueOf(tmp[0])
        val step = Integer.valueOf(tmp[1]) - start
        return Schedule(name, place, teacher, list, start, step + 1, tempDay, 0)
    }

    private fun formatDay(): Int {
        return when (day) {
            "星期一" -> 1
            "星期二" -> 2
            "星期三" -> 3
            "星期四" -> 4
            "星期五" -> 5
            "星期六" -> 6
            else -> 0
        }
    }

    /**
     * 格式化上课周
     * 将上课周的字符串转换为list
     *
     * @return 上课周的列表
     */
    private fun formatWeek(): List<Int> {
        val list: MutableList<Int> = ArrayList(16)
        if (week!!.contains("单")) {
            var i = 1
            while (i <= 15) {
                list.add(i)
                i += 2
            }
        } else if (week!!.contains("双")) {
            var i = 2
            while (i <= 16) {
                list.add(i)
                i += 2
            }
        } else {
            val weeks = week!!.split("-").toTypedArray()
            if (weeks.size == 1) {
                list.add(Integer.valueOf(weeks[0]))
            } else if (weeks.size > 1) {
                for (i in Integer.valueOf(weeks[0])..Integer.valueOf(weeks[1])) {
                    list.add(i)
                }
            }
        }
        return list
    }
}