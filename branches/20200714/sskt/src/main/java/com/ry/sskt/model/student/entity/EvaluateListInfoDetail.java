package com.ry.sskt.model.student.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/// <summary>
/// 课程
/// </summary>
@Data
@Accessors(chain = true)
public class EvaluateListInfoDetail {
    /// <summary>
    /// 评价星级
    /// </summary>
    private int EvaluateLevel;

    /// <summary>
    /// 评价内容
    /// </summary>
    private String EvaluateComment;

    /// <summary>
    /// 评价老师名称
    /// </summary>
    private String TeacherName;

    /// <summary>
    /// 评价老师头像
    /// </summary>
    private String TeacherFace;
}
