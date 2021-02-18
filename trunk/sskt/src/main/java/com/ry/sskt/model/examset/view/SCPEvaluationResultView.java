package com.ry.sskt.model.examset.view;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.examset.entity.SCPEvaluationCapability;
import com.ry.sskt.model.examset.entity.SCPEvaluationKnowledgePoint;
import com.ry.sskt.model.examset.entity.SCPKnowledgePointMasterAnalysis;
import com.ry.sskt.model.examset.entity.SCPQuestionResult;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 学生做题测评结果
 */
@Data
@Accessors(chain = true)
public class SCPEvaluationResultView {
    /// <summary>
    /// 做题结果
    /// </summary>
    @JSONField(name = "QuestionResult")
    private List<SCPQuestionResult> questionResult;

    /// <summary>
    /// 当前学科的测评得分
    /// </summary>
    @JSONField(name = "CourseScore")
    private double courseScore;

    /// <summary>
    /// 测评能力得分
    /// </summary>
    @JSONField(name = "CapabilityScores")
    private List<SCPEvaluationCapability> capabilityScores;

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
}
