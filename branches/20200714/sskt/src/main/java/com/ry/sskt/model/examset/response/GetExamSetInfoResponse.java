package com.ry.sskt.model.examset.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GetExamSetInfoResponse {

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
    /// 学科学段
    /// </summary>
    private int CourseId;
    /// <summary>
    /// 年级Id
    /// </summary>
    @JSONField(name = "GradeId")
    private int gradeId;

    /// <summary>
    /// 题目数量
    /// </summary>
    @JSONField(name = "QuestionCount")
    private int questionCount;

    /// <summary>
    /// 校验状态 0失败 1成功
    /// </summary>
    @JSONField(name = "StatusFlag")
    private int statusFlag;
}
