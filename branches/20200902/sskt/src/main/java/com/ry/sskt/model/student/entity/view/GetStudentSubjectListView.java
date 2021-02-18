package com.ry.sskt.model.student.entity.view;

import com.ry.sskt.model.subject.entity.Subject;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/// <summary>
/// 获取所有课程的评价输出对象
/// </summary>
@Data
@Accessors(chain = true)
public class GetStudentSubjectListView extends Subject {
    /// <summary>
/// 最大注册人数
/// </summary>
    private int MaxRegisterNumber;

    /// <summary>
/// 当前报名人数
/// </summary>
    private int RegisterNumber;

    /// <summary>
/// 老师名称
/// </summary>
    private String UserTrueName;

    /// <summary>
/// 学校名称
/// </summary>
    private String SchoolName;

    /// <summary>
/// 是否已报名
/// </summary>
    private Boolean IsRegister;
}
