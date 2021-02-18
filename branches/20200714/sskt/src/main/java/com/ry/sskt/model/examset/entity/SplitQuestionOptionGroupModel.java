package com.ry.sskt.model.examset.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// <summary>
/// 小题信息  拆分
/// </summary>
@Data
@Accessors(chain = true)
public class SplitQuestionOptionGroupModel {

    /// <summary>
    /// 序号
    /// </summary>
    @JSONField(name = "OrderIndex")
    private int orderIndex;

    /// <summary>
    /// 题目展示模板id
    /// </summary>
    @JSONField(name = "QuestionDisplayId")
    private int questionDisplayId;

    /// <summary>
    /// 小题题干
    /// </summary>
    @JSONField(name = "Stem")
    private String stem;

    /// <summary>
    /// 小题选项（可能为空）
    /// </summary>
    @JSONField(name = "Options")
    private List<SplitQuestionOptionModel> options;

    /// <summary>
    /// 小题答案
    /// </summary>
    @JSONField(name = "Answer")
    private List<String> answer;
}
