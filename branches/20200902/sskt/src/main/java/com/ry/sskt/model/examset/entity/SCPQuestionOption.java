package com.ry.sskt.model.examset.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

/// <summary>
/// 题目选项
/// </summary>
@Data
@Accessors(chain = true)
public class SCPQuestionOption {
    /// <summary>
    /// 题目选项ID，如 A B C D
    /// </summary>
    @JSONField(name = "QuestionOptionId")
    private String questionOptionId;

    /// <summary>
    /// 选项内容
    /// </summary>
    @JSONField(name = "QuestionOptionText")
    private String questionOptionText;

    /// <summary>
    /// 选项的顺序，越小越靠前
    /// </summary>
    @JSONField(name = "OrderIndex")
    private int orderIndex;

    public SCPQuestionOption() {
    }

    public SCPQuestionOption(String questionOptionId, String questionOptionText, int orderIndex) {
        this.questionOptionId = questionOptionId;
        this.questionOptionText = questionOptionText;
        this.orderIndex = orderIndex;
    }
}
