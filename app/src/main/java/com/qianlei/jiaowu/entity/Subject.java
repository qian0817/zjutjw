package com.qianlei.jiaowu.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.alibaba.fastjson.annotation.JSONField;
import com.zhuangfei.timetable.model.Schedule;
import com.zhuangfei.timetable.model.ScheduleEnable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * 课程的实体类
 *
 * @author qianlei
 */
@Data
@Entity
public class Subject implements ScheduleEnable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    /**
     * 课程名称
     */
    @ColumnInfo(name = "name")
    @JSONField(name = "kcmc")
    private String name;
    /**
     * 任课教师
     */
    @ColumnInfo(name = "teacher")
    @JSONField(name = "xm")
    private String teacher;
    /**
     * 上课地点
     */
    @ColumnInfo(name = "place")
    @JSONField(name = "cdmc")
    private String place;
    /**
     * 上课星期
     * 具体的格式会有
     * 1-8
     */
    @ColumnInfo(name = "week")
    @JSONField(name = "zcd")
    private String week;
    /**
     * 星期几
     * 格式为 星期X
     */
    @ColumnInfo(name = "day")
    @JSONField(name = "xqjmc")
    private String day;
    /**
     * 上课时间
     * 格式为 X-X
     */
    @ColumnInfo(name = "last")
    @JSONField(name = "jc")
    private String last;

    /**
     * 学年
     */
    @ColumnInfo(name = "year")
    @JSONField(name = "xnm")
    private String year;

    /**
     * 学年
     */
    @ColumnInfo(name = "term")
    @JSONField(name = "xqm")
    private String term;

    @Override
    public Schedule getSchedule() {
        week = week.replace("周", "");
        last = last.replace("节", "");
        String[] tmp = last.split("-");
        int start = Integer.valueOf(tmp[0]);
        int step = Integer.valueOf(tmp[1]) - start;
        int tempDay;
        switch (day) {
            case "星期一":
                tempDay = 1;
                break;
            case "星期二":
                tempDay = 2;
                break;
            case "星期三":
                tempDay = 3;
                break;
            case "星期四":
                tempDay = 4;
                break;
            case "星期五":
                tempDay = 5;
                break;
            case "星期六":
                tempDay = 6;
                break;
            default:
                tempDay = 0;
                break;
        }
        List<Integer> list = formatWeek();
        return new Schedule(name, place, teacher, list, start, step + 1, tempDay, 0);
    }

    /**
     * 格式化上课周
     * 将上课周的字符串转换为list
     *
     * @return 上课周的列表
     */
    @NotNull
    private List<Integer> formatWeek() {
        List<Integer> list = new ArrayList<>(16);
        if (week.contains("单")) {
            for (int i = 1; i <= 15; i += 2) {
                list.add(i);
            }
        } else if (week.contains("双")) {
            for (int i = 2; i <= 16; i += 2) {
                list.add(i);
            }
        } else {
            String[] weeks = week.split("-");
            if (weeks.length == 1) {
                list.add(Integer.valueOf(weeks[0]));
            } else if (weeks.length > 1) {
                for (int i = Integer.valueOf(weeks[0]); i <= Integer.valueOf(weeks[1]); i++) {
                    list.add(i);
                }
            }
        }
        return list;
    }
}
