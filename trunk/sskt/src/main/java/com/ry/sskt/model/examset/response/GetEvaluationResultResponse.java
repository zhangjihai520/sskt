package com.ry.sskt.model.examset.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.examset.entity.SCPEvaluationKnowledgePoint;
import com.ry.sskt.model.examset.entity.SCPKnowledgePointMasterAnalysis;
import com.ry.sskt.model.examset.entity.StudentAnswerResult;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// <summary>
/// 测评结果输出对象
/// </summary>
@Data
@Accessors(chain = true)
public class GetEvaluationResultResponse {
    /// <summary>
    /// 作业名称（得拼接学生姓名 和学科学段名字）
    /// </summary>
    @JSONField(name = "ExamSetName")
    private String examSetName;

    /// <summary>
    /// 总题目数量
    /// </summary>
    @JSONField(name = "QuestionCount")
    private int questionCount;

    /// <summary>
    /// 做对题目数量
    /// </summary>
    @JSONField(name = "CorrectCount")
    private int correctCount;

    /// <summary>
    /// 测评知识点得分
    /// </summary>
    @JSONField(name = "KnowledgePointScores")
    private List<SCPEvaluationKnowledgePoint> knowledgePointScores;

    /// <summary>
    /// 知识点掌握分析
    /// </summary>
    @JSONField(name = "KnowledgePointMasterAnalysis")
    private List<SCPKnowledgePointMasterAnalysis> knowledgePointMasterAnalysis;

    /// <summary>
    /// 做题结果
    /// </summary>
    @JSONField(name = "QuestionResult")
    private List
            <StudentAnswerResult> questionResult;

}
