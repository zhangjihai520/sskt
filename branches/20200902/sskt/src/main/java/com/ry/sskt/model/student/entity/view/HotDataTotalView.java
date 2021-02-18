package com.ry.sskt.model.student.entity.view;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

/// <summary>
/// 热门数据
/// </summary>
@Data
@Accessors(chain = true)
public class HotDataTotalView {
    /// <summary>
    /// 上课总数
    /// </summary>
    private int ClassAttendanceTotal;

    /// <summary>
    /// 注册总数
    /// </summary>
    private int RegistTotal;

    /// <summary>
    /// 好评值总分
    /// </summary>
    private BigDecimal PraiseSum;

    /// <summary>
    /// 好评个数
    /// </summary>
    private int PraiseCount;

    /// <summary>
    /// 评价最高分
    /// </summary>
    private static int MaxPraise = 5;


    /// <summary>
    /// 好评率
    /// </summary>
    private BigDecimal Praise;


    /// <summary>
    /// 出勤率
    /// </summary>
    private BigDecimal AttendanceRate;

    public BigDecimal getPraise() {
        if (PraiseCount == 0) {
            return new BigDecimal(0);
        } else {
            return PraiseSum.divide(new BigDecimal(PraiseCount), 1, RoundingMode.HALF_UP).setScale(1);
        }
    }

    public BigDecimal getAttendanceRate() {
        if (RegistTotal == 0) {
            return new BigDecimal(0);
        } else {
            return new BigDecimal(ClassAttendanceTotal).divide(new BigDecimal(RegistTotal), 0, RoundingMode.HALF_UP).multiply(new BigDecimal(100)).setScale(0);
        }
    }
}
