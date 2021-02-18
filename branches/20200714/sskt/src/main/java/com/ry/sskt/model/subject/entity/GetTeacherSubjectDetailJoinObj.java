package com.ry.sskt.model.subject.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 获取时间段内的学生开课情况 输出参数
 */
@Data
@Accessors(chain = true)
public class GetTeacherSubjectDetailJoinObj {
    /// <summary>
    /// 上课人数
    /// </summary>
    private int ClassAttendance;

    /// <summary>
    /// 课程名称
    /// </summary>
    private String SubjectName;

    /// <summary>
    /// 课程加密Id
    /// </summary>
    private int SubjectId;

    /// <summary>
    /// 课程开始时间  YYYY-MM-DD HH:mm
    /// </summary>
    private String BeginTime;
}
