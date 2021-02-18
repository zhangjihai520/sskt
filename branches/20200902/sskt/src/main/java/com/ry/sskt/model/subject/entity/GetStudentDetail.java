package com.ry.sskt.model.subject.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

/**
 * 学生详情
 */
@Data
@Accessors(chain = true)
public class GetStudentDetail {
    /// <summary>
    /// 学生名称
    /// </summary>
    private String StudentName;

    /// <summary>
    /// 学校名称
    /// </summary>
    private String SchoolName;

    /// <summary>
    /// 年级名称
    /// </summary>
    private String GradeName;

    /// <summary>
    /// 报名日期
    /// </summary>
    private String CreateDateTime;

    /// <summary>
    /// 用户头像
    /// </summary>
    private String UserFace;

    /// <summary>
    /// 学生Id
    /// </summary>
    private String StudentId;

    /// <summary>
    /// 评价Id 为0没有评价
    /// </summary>
    private boolean HasEvaluateRecord;
}
