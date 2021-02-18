package com.ry.sskt.model.subject.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

/**
 * 获取我的学生课程列表 输出参数
 */
@Data
@Accessors(chain = true)
public class SubjectExamSetH5 {
    /// <summary>
    /// 作业名称
    /// </summary>
    private String ExamSetName;

    /// <summary>
    /// 作业加密Id
    /// </summary>
    private String ExamSetId;

    /// <summary>
    /// 作业类型 1课前作业，2随堂练习，3课后作业
    /// </summary>
    private int ExamSetTypeId;

    /// <summary>
    /// 是否已完成
    /// </summary>
    private boolean IsFinish;

}
