package com.ry.sskt.model.examset.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

/// <summary>
/// 学生知识点掌握程度分析
/// </summary>
@Data
@Accessors(chain = true)
public class SCPKnowledgePointMasterAnalysis {

    /// <summary>
    /// 知识点名称（三级知识点名称）
    /// </summary>
    @JSONField(name="KnowledgePointName")
    private String knowledgePointName;

    /// <summary>
    /// 父知识点名称（二级知识点名称）
    /// </summary>
    @JSONField(name="ParentKnowledgePointName")
    private String parentKnowledgePointName;

    /// <summary>
    /// 掌握分析，1：已掌握，2：部分掌握，3：未掌握
    /// </summary>
    @JSONField(name="MasterAnalysis")
    private int masterAnalysis;

    /// <summary>
    /// 知识点下题目难度级别，1：容易，2：较易，3：一般，4：较难，5：困难
    /// </summary>
    @JSONField(name="KnowledgePointLevel")
    private int knowledgePointLevel;

    /// <summary>
    /// 题目数量
    /// </summary>
    @JSONField(name="NumberOfQuestion")
    private int numberOfQuestion;

    public SCPKnowledgePointMasterAnalysis(){}

    public SCPKnowledgePointMasterAnalysis(String knowledgePointName, String parentKnowledgePointName, int masterAnalysis, int knowledgePointLevel, int numberOfQuestion) {
        this.knowledgePointName = knowledgePointName;
        this.parentKnowledgePointName = parentKnowledgePointName;
        this.masterAnalysis = masterAnalysis;
        this.knowledgePointLevel = knowledgePointLevel;
        this.numberOfQuestion = numberOfQuestion;
    }
}
