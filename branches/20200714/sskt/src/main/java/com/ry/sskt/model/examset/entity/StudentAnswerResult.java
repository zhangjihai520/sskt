package com.ry.sskt.model.examset.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

/// <summary>
/// 用户的做题测评能力分析
/// </summary>
@Data
@Accessors(chain = true)
public class StudentAnswerResult extends SCPQuestionResult {
    /// <summary>
    /// 学生作答
    /// </summary>
    @JSONField(name = "StudentAnswer")
    private String studentAnswer;

    public StudentAnswerResult() {
    }

    /// <summary>
    /// 获取实例
    /// </summary>
    /// <param name="scpQuestionResult"></param>
    /// <param name="studentAnswer"></param>
    /// <returns></returns>
    public StudentAnswerResult(SCPQuestionResult scpQuestionResult, String studentAnswer) {

        this.setAnswer(scpQuestionResult.getAnswer());
        this.setCorrect(scpQuestionResult.isCorrect());
        this.setQuestionId(scpQuestionResult.getQuestionId());
        this.studentAnswer = studentAnswer;
    }
}
