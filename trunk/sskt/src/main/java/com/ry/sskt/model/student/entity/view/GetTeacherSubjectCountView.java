package com.ry.sskt.model.student.entity.view;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/// <summary>
/// 获取所有课程的评价输出对象
/// </summary>
@Data
@Accessors(chain = true)
public class GetTeacherSubjectCountView {
    /// <summary>
    /// 总数
    /// </summary>
    private int Total;

    /// <summary>
    /// 本周数量
    /// </summary>
    private int TotalWeek;

    /// <summary>
    /// 本周完成数量
    /// </summary>
    private int CompletedTotalWeek;
}
