package com.ry.sskt.model.subject.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

/**
 * 教室详情
 */
@Data
@Accessors(chain = true)
public class SubjectInfoForShowDetail {
    /// <summary>
    /// 教室名称
    /// </summary>
    private String SubjectRoomId;

    /// <summary>
    /// 教室名称
    /// </summary>
    private String SubjectRoomName;

    /// <summary>
    /// 学校名称
    /// </summary>
    private String SchoolName;

    /// <summary>
    /// 老师Id
    /// </summary>
    private String TeacherId;

    /// <summary>
    /// 老师名称
    /// </summary>
    private String TeacherName;

    /// <summary>
    /// 报名人数
    /// </summary>
    private int RegisterNumber;

    /// <summary>
    /// 是否显示助教查看详情
    /// </summary>
    private boolean IsShowRoomNumber;

    /// <summary>
    /// 上课人数
    /// </summary>
    private int ClassAttendance;
}
