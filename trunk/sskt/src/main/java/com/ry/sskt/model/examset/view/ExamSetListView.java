package com.ry.sskt.model.examset.view;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 课堂作业列表输出对象
 */
@Data
@Accessors(chain = true)
public class ExamSetListView {
    /// <summary>
    /// 作业Id
    /// </summary>
    @JSONField(name = "ExamSetId")
    private int examSetId;

    /// <summary>
    /// 课程ID
    /// </summary>
    @JSONField(name = "SubjectID")
    private int subjectId;

    /// <summary>
    /// 作业名字
    /// </summary>
    @JSONField(name = "examSetName")
    private String ExamSetName;

    /// <summary>
    /// 学科学段
    /// </summary>
    @JSONField(name = "CourseId")
    private int courseId;

    /// <summary>
    /// 年级
    /// </summary>
    @JSONField(name = "GradeId")
    private int gradeId;

    /// <summary>
    /// 作业属性
    /// </summary>
    @JSONField(name = "ExamSetTypeId")
    private int examSetTypeId;

    /// <summary>
    /// 题目数量
    /// </summary>
    @JSONField(name = "QuestionNumber")
    private int questionNumber;

    /// <summary>
    /// 作业状态
    /// </summary>
    @JSONField(name = "StatusFlag")
    private int statusFlag;

    /// <summary>
    /// 创建时间
    /// </summary>
    @JSONField(name = "CreateDateTime")
    private LocalDateTime createDateTime;

    /// <summary>
    /// 作答数
    /// </summary>
    @JSONField(name = "AnswerCount")
    private int answerCount;
}
