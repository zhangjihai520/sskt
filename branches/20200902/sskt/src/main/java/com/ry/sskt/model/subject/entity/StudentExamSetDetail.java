package com.ry.sskt.model.subject.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;
/// <summary>
/// 学生作业详情
/// </summary>
@Data
@Accessors(chain = true)
public class StudentExamSetDetail {
    /// <summary>
    /// 课程作业Id
    /// </summary>
    private String ExamSetId;

    /// <summary>
    /// 作业类型，1课前作业，2随堂练习，3课后作业
    /// </summary>
    private int ExamSetTypeId;

    /// <summary>
    /// 是否已完成
    /// </summary>
    private boolean IsFinish;
}
