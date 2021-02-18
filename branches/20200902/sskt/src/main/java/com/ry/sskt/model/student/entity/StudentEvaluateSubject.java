package com.ry.sskt.model.student.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/// <summary>
/// 助教的课程评价
/// </summary>
@Data
@Accessors(chain = true)
public class StudentEvaluateSubject {
    /// <summary>
    /// 课程id
    /// </summary>
    private int SubjectId;

    /// <summary>
    /// 课程名称
    /// </summary>
    private String SubjectName;

    /// <summary>
    /// 课程开始时间
    /// </summary>
    private LocalDateTime BeginTime;

    /// <summary>
    /// 学段学科
    /// </summary>
    private int CourseId;

    /// <summary>
    /// 主讲老师编号
    /// </summary>
    private int TeacherId;

    /// <summary>
    /// 主讲老师名称
    /// </summary>
    private String TeacherName;

    /// <summary>
    /// 图片路径
    /// </summary>
    private String ImagePath;

    /// <summary>
    /// 是否评价，0未评价，1已评价
    /// </summary>
    private int IsEvaluate;

    /// <summary>
    /// 教室id
    /// 仅学生的课程评价列表
    /// </summary>
    private int SubjectRoomId;


    /// <summary>
    /// 评价内容
    /// </summary>
    private String EvaluateComment;

    /// <summary>
    /// 老师头像
    /// </summary>
    private String TeacherFace;

    /// <summary>
    /// 评价星级
    /// </summary>
    private int EvaluateLevel;
}
