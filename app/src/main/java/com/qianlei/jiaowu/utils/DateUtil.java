package com.qianlei.jiaowu.utils;

import java.util.Calendar;

/**
 * 日期工具类
 *
 * @author qianlei
 */
public class DateUtil {
    /**
     * 获取当前日期对应的学年
     *
     * @return 当前日期对应的学年
     */
    public static int getCurYear() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        if (month < 9) {
            year--;
        }
        return year;
    }

    /**
     * 获取当前日期对应的学期
     *
     * @return 当前日期对应的学期
     */
    public static int getCurTerm() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        if (month == 7 || month == 8) {
            return 3;
        } else if (month < 7 && month > 1) {
            return 2;
        } else {
            return 1;
        }
    }
}
