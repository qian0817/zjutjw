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
 * 教务系统传入的JSON格式如下
 * cd_id": "19867",
 * "cdmc": "广B207",
 * "date": "二○二○年二月一日",
 * "dateDigit": "2020年2月1日",
 * "day": "1",
 * "jc": "1-2节",
 * "jcor": "1-2",
 * "jcs": "1-2",
 * "jgh_id": "1863",
 * "jgpxzd": "1",
 * "jxb_id": "8883A788A8154D79E053A11310AC294E",
 * "jxbmc": "数据结构-0010",
 * "kch_id": "17378",
 * "kcmc": "数据结构",
 * "kcxszc": "理论:48,实验:16",
 * "khfsmc": "考试",
 * "listnav": "false",
 * "localeKey": "zh_CN",
 * "month": "2",
 * "oldjc": "3",
 * "oldzc": "65535",
 * "pageable": true,
 * "pkbj": "1",
 * "queryModel":{"currentPage": 1, "currentResult": 0, "entityOrField": false, "limit": 15, "offset": 0,…},
 * "rangeable": true,
 * "rsdzjs": 0,
 * "sxbj": "0",
 * "totalResult": "0",
 * "userModel":{"monitor": false, "roleCount": 0, "roleKeys": "", "roleValues": "",…},
 * "xf": "4.0",
 * "xkbz": "无",
 * "xm": "陈志杨",
 * "xnm": "2019",
 * "xqdm": "0",
 * "xqh1": "02,",
 * "xqh_id": "02",
 * "xqj": "1",
 * "xqjmc": "星期一",
 * "xqm": "3",
 * "xqmc": "屏峰校区",
 * "xsdm": "01",
 * "xslxbj": "◆",
 * "year": "2020",
 * "zcd": "1-16周",
 * "zcmc": "副教授",
 * "zhxs": "4",
 * "zxs": "64",
 * "zyfxmc": "无方向"
 * 课程的实体类
 *
 * @author qianlei
 */
@Data
@Entity
public class Subject implements ScheduleEnable {
    @PrimaryKey(autoGenerate = true)
    public int id;
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
