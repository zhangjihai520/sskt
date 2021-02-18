package com.ry.sskt.model.subject.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 教室详情
 */
@Data
@Accessors(chain = true)
public class SubjectRoomDetail {
    /// <summary>
    /// 教室Id
    /// </summary>
    private String SubjectRoomId;

    /// <summary>
    /// 教室名称
    /// </summary>
    private String SubjectRoomName;

    /// <summary>
    /// 教室名称
    /// </summary>
    private String SchoolName;

    /// <summary>
    /// 助教老师Id
    /// </summary>
    private String HelpTeacherId;

    /// <summary>
    /// 助教老师名称
    /// </summary>
    private String HelpTeacherName;

    /// <summary>
    /// 最大报名人数
    /// </summary>
    private int MaxRegisterNumber;
}
