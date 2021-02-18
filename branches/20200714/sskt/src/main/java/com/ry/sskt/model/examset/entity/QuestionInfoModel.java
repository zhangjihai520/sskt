package com.ry.sskt.model.examset.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 题目信息
 */
@Data
@Accessors(chain = true)
public class QuestionInfoModel {

    /// <summary>
    /// 题目ID
    /// </summary>
    @JSONField(name = "QuestionId")
    private int questionId;

    /// <summary>
    /// 难度
    /// </summary>
    @JSONField(name = "QuestionLevel")
    private int questionLevel;

    /// <summary>
    /// 题型
    /// </summary>
    @JSONField(name = "QuestionCategoryId")
    private int questionCategoryId;

    /// <summary>
    /// 题型名称
    /// </summary>
    @JSONField(name = "QuestionCategoryName")
    private String questionCategoryName;

    /// <summary>
    /// 题文
    /// </summary>
    @JSONField(name = "Stem")
    private String stem;

    /// <summary>
    /// 选项
    /// </summary>
    @JSONField(name = "Options")
    private List<String> options;

    /// <summary>
    /// 答案
    /// </summary>
    @JSONField(name = "Answer")
    private List<String> answer;

    /// <summary>
    /// 解析
    /// </summary>
    @JSONField(name = "Analysis")
    private String analysis;

    /// <summary>
    /// 题目来源
    /// </summary>
    @JSONField(name = "QuestionSource")
    private String questionSource;

    /// <summary>
    /// 知识点id
    /// </summary>
    @JSONField(name = "KnowledgePointId")
    private int knowledgePointId;

    /// <summary>
    /// 解题方法标签id
    /// </summary>
    @JSONField(name = "AnalyticMethod")
    private String analyticMethod;

    /// <summary>
    /// 能力结构标签id
    /// </summary>
    @JSONField(name = "AbilityStructure")
    private String abilityStructure;

    /// <summary>
    /// 题目展示模板id
    /// </summary>
    @JSONField(name = "QuestionDisplayId")
    private int questionDisplayId;

    /// <summary>
    /// 章节
    /// </summary>
    @JSONField(name = "SectionMappings")
    private List<Integer> sectionMappings;

    /// <summary>
    /// 创建时间
    /// </summary>
    @JSONField(name = "CreateDateTime")
    private String createDateTime;

    /// <summary>
    /// 更新时间
    /// </summary>
    @JSONField(name = "UpdateDateTime")
    private String updateDateTime;

    /// <summary>
    /// 知识点集合
    /// </summary>
    @JSONField(name = "KnowledgePointIds")
    private List<Integer> knowledgePointIds;

    /// <summary>
    /// 题文 拆分
    /// </summary>
    @JSONField(name = "SplitQuestionStem")
    private String splitQuestionStem;

    /// <summary>
    /// 小题集合 拆分
    /// </summary>
    @JSONField(name = "SplitQuestionInfo")
    private List<SplitQuestionOptionGroupModel> splitQuestionInfo;

    /// <summary>
    /// 权重
    /// </summary>
    @JSONField(name = "Weight")
    private double weight;

    /// <summary>
    /// 听力音频Url
    /// </summary>
    @JSONField(name = "QuestionAudioUrl")
    private String questionAudioUrl;
}
