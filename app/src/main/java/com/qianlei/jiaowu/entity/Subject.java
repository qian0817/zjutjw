package com.qianlei.jiaowu.entity;

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
public class Subject implements ScheduleEnable {
    /**
     * 课程名称
     */
    @JSONField(name = "kcmc")
    private String name;
    /**
     * 任课教师
     */
    @JSONField(name = "xm")
    private String teacher;
    /**
     * 上课地点
     */
    @JSONField(name = "cdmc")
    private String place;
    /**
     * 上课星期
     * 具体的格式会有
     * 1-8
     */
    @JSONField(name = "zcd")
    private String week;
    /**
     * 星期几
     * 格式为 星期X
     */
    @JSONField(name = "xqjmc")
    private String day;
    /**
     * 上课时间
     * 格式为 X-X
     */
    @JSONField(name = "jc")
    private String last;

    public void setWeek(String week) {
        week = week.replace("周", "");
        this.week = week;
    }

    public void setLast(String last) {
        last = last.replace("节", "");
        this.last = last;
    }

    @Override
    public Schedule getSchedule() {
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
