package com.ry.sskt.model.subject.entity.view;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 获取课程列表班级信息详情
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

    public SubjectStatisticsInfo() {
    }
}
