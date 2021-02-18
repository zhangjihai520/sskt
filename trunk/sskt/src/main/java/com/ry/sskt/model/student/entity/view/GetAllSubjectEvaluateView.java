package com.ry.sskt.model.student.entity.view;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/// <summary>
/// 获取所有课程的评价输出对象
/// </summary>
@Data
@Accessors(chain = true)
public class GetAllSubjectEvaluateView {
    /// <summary>
    /// 课程加密Id
    /// </summary>
    private int SubjectId;

    /// <summary>
    /// 课程名称
    /// </summary>
    private String SubjectName;

    /// <summary>
    /// 课程开课时间
    /// </summary>
    private LocalDateTime BeginTime;

    /// <summary>
    /// 评价数量
    /// </summary>
    private int EvaluateCount;
}
