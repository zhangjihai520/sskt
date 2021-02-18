package com.ry.sskt.model.student.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/// <summary>
/// 助教的课程评价
/// </summary>
@Data
@Accessors(chain = true)
public class TeacherEvaluateSubject {
    /// <summary>
    /// 课程id
    /// </summary>
    private int SubjectId ;

    /// <summary>
    /// 课程名称
    /// </summary>
    private String SubjectName ;

    /// <summary>
    /// 课程开始时间
    /// </summary>
    private LocalDateTime BeginTime ;

    /// <summary>
    /// 学段学科
    /// </summary>
    private int GradeId ;

    /// <summary>
    /// 学段学科
    /// </summary>
    private int CourseId ;

    /// <summary>
    /// 主讲老师编号
    /// </summary>
    private int TeacherId ;

    /// <summary>
    /// 主讲老师名称
    /// </summary>
    private String TeacherName ;

    /// <summary>
    /// 图片路径
    /// </summary>
    private String ImagePath ;

    /// <summary>
    /// 是否评价，0未评价，1已评价
    /// </summary>
    private int IsEvaluate ;
}
