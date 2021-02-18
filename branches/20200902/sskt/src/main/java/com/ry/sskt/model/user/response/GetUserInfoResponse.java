package com.ry.sskt.model.user.response;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 【用户信息】获取用户基本信息 返回参数
 */
@Data
@Accessors(chain = true)
public class GetUserInfoResponse {
    /// <summary>
    /// 邮箱
    /// </summary>
    private String Email;

    /// <summary>
    /// 姓名
    /// </summary>
    private String UserTrueName;

    /// <summary>
    /// 学校
    /// </summary>
    private String SchoolName;

    /// <summary>
    /// 年级id
    /// </summary>
    private int GradeId;

    /// <summary>
    /// 年级名称
    /// </summary>
    private String GradeName;

    /// <summary>
    /// 个人简介
    /// </summary>
    private String Comment;

    /// <summary>
    /// 用户头像
    /// </summary>
    private String UserFace;

    /// <summary>
    /// 密保手机
    /// </summary>
    private String Phone;

    /// <summary>
    /// 作业数量，仅学生用户
    /// </summary>
    private int ExamCount;

    /// <summary>
    /// 课程数量，仅学生用户
    /// </summary>
    private int SubjectCount;
}
