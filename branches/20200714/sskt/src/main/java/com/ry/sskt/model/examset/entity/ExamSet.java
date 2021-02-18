package com.ry.sskt.model.examset.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
public class ExamSet {
    /// <summary>
    /// 作业id
    /// </summary>
    @JSONField(name="ExamSetId")
    private int examSetId ;

    /// <summary>
    /// 课程id
    /// </summary>
    @JSONField(name="SubjectId")
    private int subjectId ;

    /// <summary>
    /// 作业名称
    /// </summary>
    @JSONField(name="ExamSetName")
    private String examSetName ;

    /// <summary>
    /// 学段学科
    /// </summary>
    @JSONField(name="CourseId")
    private int courseId ;

    /// <summary>
    /// 适用年级
    /// </summary>
    @JSONField(name="GradeId")
    private int gradeId ;

    /// <summary>
    /// 作业类型，1课前作业，2随堂练习，3课后作业
    /// </summary>
    @JSONField(name="ExamSetTypeId")
    private int examSetTypeId ;

    /// <summary>
    /// 题目数量
    /// </summary>
    @JSONField(name="QuestionNumber")
    private int questionNumber ;

    /// <summary>
    /// 成绩
    /// </summary>
    @JSONField(name="Score")
    private BigDecimal score ;

    /// <summary>
    /// 魔题库题集id
    /// </summary>
    @JSONField(name="MotkExamSetId")
    private String motkExamSetId ;

    /// <summary>
    /// 题目id集合，已英文逗号分隔
    /// </summary>
    @JSONField(name="QuestionIds")
    private String questionIds ;

    /// <summary>
    /// 状态，0删除，1待上架，2已上架
    /// </summary>
    @JSONField(name="StatusFlag")
    private int statusFlag ;

    /// <summary>
    /// 创建时间
    /// </summary>
    @JSONField(name="CreateDateTime")
    private Date createDateTime ;

    /// <summary>
    /// 更新时间
    /// </summary>
    @JSONField(name="UpdateDateTime")
    private Date updateDateTime ;
}
