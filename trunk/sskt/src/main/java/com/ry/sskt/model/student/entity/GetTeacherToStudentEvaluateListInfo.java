package com.ry.sskt.model.student.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/// <summary>
/// 用户消息
/// </summary>
@Data
@Accessors(chain = true)
public class GetTeacherToStudentEvaluateListInfo {
    /// <summary>
    /// 课程名称
    /// </summary>
    private String SubjectName;

    /// <summary>
    /// 课程封面地址
    /// </summary>
    private String ImagePath;

    /// <summary>
    /// 课程开始时间
    /// </summary>
    private String BeginTime;
    /// <summary>
    /// 课程短码
    /// </summary>
    private String ShortCode;

    private String CourseName;

    /// <summary>
    /// <summary>
    /// 评价详情列表
    /// </summary>
    private List<EvaluateListInfoDetail> EvaluateInfoList;
}
