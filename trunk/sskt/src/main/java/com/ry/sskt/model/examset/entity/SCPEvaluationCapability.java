package com.ry.sskt.model.examset.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

/// <summary>
/// 用户的做题测评能力分析
/// </summary>
@Data
@Accessors(chain = true)
public class SCPEvaluationCapability {
    /// <summary>
    /// 能力Id
    /// </summary>
    @JSONField(name="CapabilityId")
    private int capabilityId;

    /// <summary>
    /// 能力名称
    /// </summary>
    @JSONField(name="CapabilityName")
    private String capabilityName;

    /// <summary>
    /// 能力得分
    /// </summary>
    @JSONField(name="Score")
    private double score;

    public SCPEvaluationCapability(){}

    public SCPEvaluationCapability(int capabilityId, String capabilityName, double score) {
        this.capabilityId = capabilityId;
        this.capabilityName = capabilityName;
        this.score = score;
    }
}
