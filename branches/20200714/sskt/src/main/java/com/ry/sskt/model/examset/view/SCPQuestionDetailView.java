package com.ry.sskt.model.examset.view;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.examset.entity.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// <summary>
/// 题目详情
/// </summary>
@Data
@Accessors(chain = true)
public class SCPQuestionDetailView {
    /// <summary>
    /// 题目集合
    /// </summary>
    @JSONField(name = "Questions")
    private List<SCPQuestionDetail> questions;
}
