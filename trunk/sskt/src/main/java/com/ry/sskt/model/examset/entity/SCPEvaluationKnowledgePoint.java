package com.ry.sskt.model.examset.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

/// <summary>
/// 用户的做题测评知识点
/// </summary>
@Data
@Accessors(chain = true)
public class SCPEvaluationKnowledgePoint {
    /// <summary>
    /// 知识点Id
    /// </summary>
    @JSONField(name="KnowledgePointId")
    private int knowledgePointId;

    /// <summary>
    /// 知识点名称
    /// </summary>
    @JSONField(name="KnowledgePointName")
    private String knowledgePointName;

    /// <summary>
    /// 知识点得分
    /// </summary>
    @JSONField(name="Score")
    private double score;

    public SCPEvaluationKnowledgePoint(){}

    public SCPEvaluationKnowledgePoint(int knowledgePointId, String knowledgePointName, double score) {
        this.knowledgePointId = knowledgePointId;
        this.knowledgePointName = knowledgePointName;
        this.score = score;
    }
}
