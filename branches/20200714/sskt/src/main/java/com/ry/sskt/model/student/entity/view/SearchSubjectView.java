package com.ry.sskt.model.student.entity.view;

import com.ry.sskt.model.subject.entity.Subject;
import lombok.Data;
import lombok.experimental.Accessors;

/// <summary>
/// 获取所有课程的评价输出对象
/// </summary>
@Data
@Accessors(chain = true)
public class SearchSubjectView extends Subject {
    /// <summary>
    /// 老师名称
    /// </summary>
    private String TeacherName;

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
    private Boolean IsRegister;
}
