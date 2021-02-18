package com.ry.sskt.model.examset.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

/// <summary>
///做题统计结果
/// </summary>
@Data
@Accessors(chain = true)
public class QuestionAnswerStatisticsResult extends QuestionDetail {
    /// <summary>
    /// 答对人数
    /// </summary>
    @JSONField(name = "CorrectNum")
    private int CorrectNum;
    /// <summary>
    /// 答错人数
    /// </summary>
    @JSONField(name = "ErrorNum")
    private int ErrorNum;
}
