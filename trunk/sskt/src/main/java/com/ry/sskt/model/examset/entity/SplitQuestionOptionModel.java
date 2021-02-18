package com.ry.sskt.model.examset.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 题目选项信息
 */
@Data
@Accessors(chain = true)
public class SplitQuestionOptionModel {

    /// <summary>
    /// 选项Id
    /// </summary>
    @JSONField(name = "OptionId")
    private String optionId;

    /// <summary>
    /// 选项内容
    /// </summary>
    @JSONField(name = "OptionText")
    private String optionText;

}
