package com.ry.sskt.model.student.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/// <summary>
/// 课程
/// </summary>
@Data
@Accessors(chain = true)
public class SubjectInfo {
    /// <summary>
    /// 课程ID，加密
    /// </summary>
    private String SubjectId;

    /// <summary>
    /// 课程名称
    /// </summary>
    private String SubjectName ;

    /// <summary>
    /// 课程图片
    /// </summary>
    private String ImagePath ;

    /// <summary>
    /// 学科
    /// </summary>
    private String CourseName ;

    /// <summary>
    /// 学段短码学科
    /// </summary>
    private String ShortCode ;

    /// <summary>
    /// 主讲老师id,加密
    /// </summary>
    private String TeacherId ;

    /// <summary>
    /// 主讲老师名字
    /// </summary>
    private String TeacherName ;

    /// <summary>
    /// 课程时间
    /// </summary>
    private String BeginTime ;

    /// <summary>
    /// 课程状态<see cref="VideoClass.WebApi.Models.Enum.SubjectStatusEnum">
    /// </summary>
    private int StatusFlag ;

    /// <summary>
    /// 评价状态，1已评价,0未评价
    /// </summary>
    private int IsEvaluate ;

    /// <summary>
    /// 教室id，加密
    /// </summary>
    private String SubjectRoomId ;

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
