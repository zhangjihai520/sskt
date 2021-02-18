package com.ry.sskt.model.student.entity.view;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/// <summary>
/// 获取上课人数
/// </summary>
@Data
@Accessors(chain = true)
public class GetClassAttendanceView {
    /// <summary>
    /// 课程Id
    /// </summary>
    private int SubjectId;

    /// <summary>
    /// 报名人数
    /// </summary>
    private int RegistCount;

    /// <summary>
    /// 缺勤人数
    /// </summary>
    private int AbsentCount;

    /// <summary>
    /// 上课人数
    /// </summary>
    private int ClassAttendance;

    public int getClassAttendance() {
        return this.RegistCount - this.AbsentCount;
    }
}
