package com.ry.sskt.model.openapi.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 学生做题对象
 */
@Data
@Accessors(chain = true)
public class StudentQuestion {

    /// <summary>
    /// 题目Id
    /// </summary>
    @JSONField(name="QuestionId")
    private int questionId;

    /// <summary>
    /// 学生答案
    /// </summary>
    @JSONField(name="StudentAnswer")
    private String studentAnswer;

    public StudentQuestion(){}

    public StudentQuestion(int questionId, String studentAnswer) {
        this.questionId = questionId;
        this.studentAnswer = studentAnswer;
    }
}
