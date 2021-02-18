package com.ry.sskt.model.student.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/// <summary>
/// 题目答案
/// </summary>
@Data
@Accessors(chain = true)
public class QuestionAnswer {
    /// <summary>
    /// 消息ID，加密
    /// </summary>
    private String QuestionId;

    /// <summary>
    /// 消息源用户id，加密
    /// </summary>
    private String Answer;


}
