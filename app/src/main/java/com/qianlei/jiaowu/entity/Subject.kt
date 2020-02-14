package com.qianlei.jiaowu.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alibaba.fastjson.annotation.JSONField
import com.zhuangfei.timetable.model.Schedule
import com.zhuangfei.timetable.model.ScheduleEnable

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
        val tempDay = formatDay()
        val list = formatWeek()
        var start = 0
        var step = 0
        val tempLast = last
        if (tempLast != null) {
            val tmp = tempLast.replace("节", "").split("-")
            start = Integer.valueOf(tmp[0])
            step = Integer.valueOf(tmp[1]) - start
        }
        val schedule = Schedule(name, place, teacher, list, start, step + 1, tempDay, 0)
        schedule.extras["week"] = week
        schedule.extras["last"] = last
        return schedule
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
        var tempWeek = week ?: return ArrayList()
        tempWeek = tempWeek.replace("周", "")
        val list: MutableList<Int> = ArrayList(16)
        if (tempWeek.contains("单")) {
            var i = 1
            while (i <= 15) {
                list.add(i)
                i += 2
            }
        } else if (tempWeek.contains("双")) {
            var i = 2
            while (i <= 16) {
                list.add(i)
                i += 2
            }
        } else {
            val weeks = tempWeek.split("-").toTypedArray()
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