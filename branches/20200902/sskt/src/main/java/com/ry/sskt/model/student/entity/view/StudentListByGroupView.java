package com.ry.sskt.model.student.entity.view;

import com.ry.sskt.model.common.entity.User;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/// <summary>
/// 课程学生Count
/// </summary>
@Data
@Accessors(chain = true)
public class StudentListByGroupView {
    /// <summary>
    /// 学生姓名
    /// </summary>
    private String UserTrueName;

    /// <summary>
    /// 报名时间
    /// </summary>
    private LocalDateTime CreateDateTime;

    /// <summary>
    /// 学生ID
    /// </summary>
    private int UserId;

    /// <summary>
    /// 学生在哪个老师分组里面
    /// </summary>
    private String TeacherName;

    /// <summary>
    /// 教室Id
    /// </summary>
    private int SubjectRoomId;
}
