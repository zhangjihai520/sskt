package com.ry.sskt.model.examset.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/// <summary>
/// 闪测评题目
/// </summary>
@Data
@Accessors(chain = true)
public class QuestionDetail {
    /// <summary>
    /// 题目Id
    /// </summary>
    @JSONField(name = "QuestionId")
    private int questionId;

    /// <summary>
    /// 题目序号（保存题集题目数据时按照序号排序在保存）
    /// </summary>
    @JSONField(name = "Order")
    private int order;

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
}
