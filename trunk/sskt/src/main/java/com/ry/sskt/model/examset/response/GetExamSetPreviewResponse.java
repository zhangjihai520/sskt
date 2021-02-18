package com.ry.sskt.model.examset.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.examset.entity.QuestionDetail;
import com.ry.sskt.model.examset.entity.SCPEvaluationKnowledgePoint;
import com.ry.sskt.model.examset.entity.SCPKnowledgePointMasterAnalysis;
import com.ry.sskt.model.examset.entity.StudentAnswerResult;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// <summary>
/// 测评结果输出对象
/// </summary>
@Data
@Accessors(chain = true)
public class GetExamSetPreviewResponse {
    /// <summary>
    /// 作业名称
    /// </summary>
    @JSONField(name = "ExamSetName")
    private String examSetName;

    /// <summary>
    /// 枚举 状态 1待上架，2已上架
    /// </summary>
    @JSONField(name = "StatusFlag")
    private int statusFlag;

    /// <summary>
    /// 题目集合
    /// </summary>
    @JSONField(name = "Questions")
    private List<QuestionDetail> questions;
}
