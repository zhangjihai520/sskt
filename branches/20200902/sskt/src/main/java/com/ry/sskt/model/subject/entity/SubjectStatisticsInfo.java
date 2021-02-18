package com.ry.sskt.model.subject.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 * 课程统计部分信息
 */
@Data
@Accessors(chain = true)
public class SubjectStatisticsInfo {
    /// <summary>
    /// 上课人次
    /// </summary>
    private int ClassAttendance;

    /// <summary>
    /// 出勤率
    /// </summary>
    private BigDecimal AttendanceRate;

    /// <summary>
    /// 好评度
    /// </summary>
    private BigDecimal Praise;
}
