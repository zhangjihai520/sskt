package com.ry.sskt.model.examset.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GetExamSetListDetail {
    /// <summary>
    /// 作业加密Id
    /// </summary>
    @JSONField(name = "ExamSetId")
    private String examSetId;

    /// <summary>
    /// 作业名字
    /// </summary>
    @JSONField(name = "ExamSetName")
    private String examSetName;

    /// <summary>
    /// 学科学段
    /// </summary>
    @JSONField(name = "CourseName")
    private String courseName;

    /// <summary>
    /// 年级
    /// </summary>
    @JSONField(name = "GradeName")
    private String gradeName;

    /// <summary>
    /// 作业属性
    /// </summary>
    @JSONField(name = "ExamSetTypeId")
    private int examSetTypeId;

    /// <summary>
    /// 题目数量
    /// </summary>
    @JSONField(name = "QuestionCount")
    private int questionCount;

    /// <summary>
    /// 作业状态
    /// </summary>
    @JSONField(name = "StatusFlag")
    private int statusFlag;

    /// <summary>
    /// 创建时间
    /// </summary>
    @JSONField(name = "CreateTime")
    private String createTime;

    /// <summary>
    /// 作答数
    /// </summary>
    @JSONField(name = "AnswerCount")
    private int answerCount;


}
