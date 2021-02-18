package com.ry.sskt.model.examset.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;
/// <summary>
/// 题目做题结果
/// </summary>
@Data
@Accessors(chain = true)
public class SCPQuestionResult {
    /// <summary>
    /// 题目Id
    /// </summary>
    @JSONField(name="QuestionId")
    private int questionId;

    /// <summary>
    /// 是否正确
    /// </summary>
    @JSONField(name="IsCorrect")
    private boolean isCorrect;

    /// <summary>
    /// 正确答案
    /// </summary>
    @JSONField(name="Answer")
    private String answer;

    public SCPQuestionResult(){}

    public SCPQuestionResult(int questionId, boolean isCorrect, String answer) {
        this.questionId = questionId;
        this.isCorrect = isCorrect;
        this.answer = answer;
    }
}
