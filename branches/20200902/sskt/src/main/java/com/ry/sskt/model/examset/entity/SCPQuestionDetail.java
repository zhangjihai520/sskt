package com.ry.sskt.model.examset.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// <summary>
/// 题目
/// </summary>
@Data
@Accessors(chain = true)
public class SCPQuestionDetail {

    /// <summary>
    /// 题目Id
    /// </summary>
    @JSONField(name = "QuestionId")
    private int questionId;

    /// <summary>
    /// 题干内容
    /// </summary>
    @JSONField(name = "QuestionContent")
    private String questionContent;

    /// <summary>
    /// 题目选项
    /// </summary>
    @JSONField(name = "Options")
    private List<SCPQuestionOption> options;

    /// <summary>
    /// 题目答案
    /// </summary>
    @JSONField(name = "Answer")
    private String answer;

    /// <summary>
    /// 题目解析内容
    /// </summary>
    @JSONField(name = "Analysis")
    private String analysis;

    /// <summary>
    /// 知识点名称集合
    /// </summary>
    @JSONField(name = "KnowledgePointNames")
    public List<String> knowledgePointNames;
    public SCPQuestionDetail() {
    }

}
