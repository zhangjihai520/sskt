package com.ry.sskt.model.teacher.entity;

import lombok.Data;
import lombok.experimental.Accessors;
/// <summary>
/// 课程信息
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
    private String SubjectName;

    /// <summary>
    /// 课程图片
    /// </summary>
    private String Image;

    /// <summary>
    /// 年级
    /// </summary>
    private String GradeName;

    /// <summary>
    /// 学科
    /// </summary>
    private String CourseName;

    /// <summary>
    /// 主讲老师id,加密
    /// </summary>
    private String TeacherId;

    /// <summary>
    /// 主讲老师名字
    /// </summary>
    private String TeacherName;

    /// <summary>
    /// 课程时间
    /// </summary>
    private String BeginTime;

    /// <summary>
    /// 课程状态<see cref="VideoClass.WebApi.Models.Enum.SubjectStatusEnum">
    /// </summary>
    private int StatusFlag;

    /// <summary>
    /// 评价状态，1已评价,0未评价
    /// </summary>
    private int IsEvaluate;
}
