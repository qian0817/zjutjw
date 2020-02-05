package com.qianlei.jiaowu.utils;

import com.qianlei.jiaowu.entity.Score;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * @author qianlei
 */
public class ScoreUtil {
    private ScoreUtil() {
    }

    @NotNull
    public static String getGpa(@Nullable List<Score> scoreList) {
        if (scoreList == null || scoreList.size() == 0) {
            return "暂无成绩信息";
        }
        BigDecimal ans = new BigDecimal("0");
        BigDecimal all = new BigDecimal("0");
        for (Score score : scoreList) {
            if (score != null && score.getGradePoint() != null && score.getGradePoint() != null) {
                ans = ans.add(new BigDecimal(score.getCredit()).multiply(new BigDecimal(score.getGradePoint())));
                all = all.add(new BigDecimal(score.getCredit()));
            }
        }
        if (all.equals(BigDecimal.ZERO)) {
            return "暂无绩点信息";
        }
        return ans.divide(all, RoundingMode.HALF_UP).setScale(3, RoundingMode.HALF_UP).toString();
    }
}
