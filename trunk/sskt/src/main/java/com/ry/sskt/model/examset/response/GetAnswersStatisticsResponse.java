package com.ry.sskt.model.examset.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.examset.entity.QuestionAnswerStatisticsResult;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/// <summary>
/// 测评结果输出对象
/// </summary>
@Data
@Accessors(chain = true)
public class GetAnswersStatisticsResponse {
    /// <summary>
    /// 上课人数
    /// </summary>
    @JSONField(name = "StudentCount")
    private int studentCount;

    /// <summary>
    /// 作答人数
    /// </summary>
    @JSONField(name = "AnswerCount")
    private int answerCount;

    /// <summary>
    /// 未作答人数
    /// </summary>
    @JSONField(name = "UnAnswerCount")
    private int unAnswerCount;

    /// <summary>
    /// 做题结果数据
    /// </summary>
    @JSONField(name = "QuestionResults")
    private List<QuestionAnswerStatisticsResult> questionResults;


    public GetAnswersStatisticsResponse() {
        this.questionResults = new ArrayList<>();
    }
}
