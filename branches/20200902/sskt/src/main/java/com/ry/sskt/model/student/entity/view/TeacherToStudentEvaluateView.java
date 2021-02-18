package com.ry.sskt.model.student.entity.view;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/// <summary>
/// 老师对学生的评价
/// </summary>
@Data
@Accessors(chain = true)
public class TeacherToStudentEvaluateView {
    /// <summary>
    /// 课程Id
    /// </summary>
    private int SubjectId;

    /// <summary>
    /// 评价星级
    /// </summary>
    private int EvaluateLevel;

    /// <summary>
    /// 评价内容
    /// </summary>
    private String  EvaluateComment;

    /// <summary>
    /// 课程名称
    /// </summary>
    private String SubjectName;

    /// <summary>
    /// 评价老师名称
    /// </summary>
    private String TeacherName;

    /// <summary>
    /// 评价老师名称
    /// </summary>
    private String TeacherFace;

    /// <summary>
    /// 课程封面地址
    /// </summary>
    private String  ImagePath;

    /// <summary>
    /// 课程开始时间
    /// </summary>
    private String  BeginTime;
    /// <summary>
    /// 学科Id
    /// </summary>
    private int CourseId;
}
