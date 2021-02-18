package com.ry.sskt.model.openapi.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.examset.entity.QuestionInfoModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 题集详细信息
 */
@Data
@Accessors(chain = true)
public class TeacherExamQuestionModel {
    /// <summary>
    /// 题目信息
    /// </summary>
    @JSONField(name = "Question")
    private QuestionInfoModel question;

    /// <summary>
    /// 题目在题集的顺序
    /// </summary>
    @JSONField(name = "Order")
    private int order;

    /// <summary>
    /// 题目的分数
    /// </summary>
    @JSONField(name = "Score")
    private double score;
}
