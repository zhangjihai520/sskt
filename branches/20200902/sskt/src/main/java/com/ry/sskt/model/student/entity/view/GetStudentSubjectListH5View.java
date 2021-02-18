package com.ry.sskt.model.student.entity.view;

import com.ry.sskt.model.subject.entity.Subject;
import lombok.Data;
import lombok.experimental.Accessors;

/// <summary>
/// 类型名称
/// </summary>
@Data
@Accessors(chain = true)
public class GetStudentSubjectListH5View {
    /// <summary>
    /// 课程ID
    /// </summary>
    private int SubjectId;

    /// <summary>
    /// 课程名称
    /// </summary>
    private String SubjectName;

    /// <summary>
    /// 作业Id
    /// </summary>
    private int ExamSetId;

    /// <summary>
    /// 作业名称
    /// </summary>
    private String ExamSetName;

    /// <summary>
    /// 课程ID
    /// </summary>
    private int ExamSetTypeId;

    /// <summary>
    /// 0未完成 1已经完成
    /// </summary>
    private int IsFinish;

    /// <summary>
    /// 学科ID
    /// </summary>
    private int CourseId;
}
