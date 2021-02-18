package com.ry.sskt.model.student.entity.view;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/// <summary>
/// 课程学生Count
/// </summary>
@Data
@Accessors(chain = true)
public class SubjectStudentCountView {
    /// <summary>
    /// 课程ID
    /// </summary>
    private int SubjectId;

    /// <summary>
    /// 课程开课时间
    /// </summary>
    private LocalDateTime BeginTime;

    /// <summary>
    /// 总人数
    /// </summary>
    private int TotalCount;

    /// <summary>
    /// 出勤人数
    /// </summary>
    private int AbsentCount;

    public String BeginTimeFomatToMMdd() {
        return BeginTime.format(DateTimeFormatter.ofPattern("MM/dd"));
    }
}
