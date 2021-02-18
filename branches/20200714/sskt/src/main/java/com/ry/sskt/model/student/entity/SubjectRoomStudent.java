package com.ry.sskt.model.student.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/// <summary>
/// 学生报名表
/// </summary>
@Data
@Accessors(chain = true)
public class SubjectRoomStudent {
    /// <summary>
    /// 教室id
    /// </summary>
    private int subjectRoomId;

    /// <summary>
    /// 学生id
    /// </summary>
    private int userId;

    /// <summary>
    /// 课程id
    /// </summary>
    private int subjectId;

    /// <summary>
    /// 是否缺席，0到席，1缺席
    /// </summary>
    private int isAbsent;

    /// <summary>
    /// 是否评价，0未评价，1已评价
    /// </summary>
    private int isEvaluate;

    /// <summary>
    /// 状态，0删除，1未删除
    /// </summary>
    private int statusFlag;

    /// <summary>
    /// 创建时间
    /// </summary>
    private LocalDateTime createDateTime;

    /// <summary>
    /// 更新时间
    /// </summary>
    private LocalDateTime updateDateTime;
}
