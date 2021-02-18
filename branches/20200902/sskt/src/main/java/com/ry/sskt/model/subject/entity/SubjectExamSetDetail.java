package com.ry.sskt.model.subject.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 作业详情
 */
@Data
@Accessors(chain = true)
public class SubjectExamSetDetail {
    /// <summary>
    /// 作业Id
    /// </summary>
    private String ExamSetId;

    /// <summary>
    /// 作业名称
    /// </summary>
    private String ExamSetName;

    /// <summary>
    /// 作业类型 <see cref="ExamSetTypeEnum"/>
    /// </summary>
    private int ExamSetTypeId;

    /// <summary>
    /// 作答数
    /// </summary>
    private int AnswerNumber;
}
