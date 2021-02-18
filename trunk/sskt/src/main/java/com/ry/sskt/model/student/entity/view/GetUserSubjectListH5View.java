package com.ry.sskt.model.student.entity.view;

import com.ry.sskt.model.subject.entity.Subject;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/// <summary>
/// 类型名称
/// </summary>
@Data
@Accessors(chain = true)
public class GetUserSubjectListH5View extends Subject {
    /// <summary>
    /// 学校名称,仅学生有
    /// </summary>
    private String SchoolName;

    /// <summary>
    /// 教室名称,仅学生有
    /// </summary>
    private String SubjectRoomName;

    /// <summary>
    /// 报名人数
    /// </summary>
    private int RealRegisterNumber;

    /// <summary>
    /// 最大报名人数
    /// </summary>
    private int MaxRegisterNumber;

    /// <summary>
    /// 是否已报名
    /// </summary>
    private boolean IsRegister;

    public String BeginTimeFomatToyyyyMMdd() {
        return getBeginTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
