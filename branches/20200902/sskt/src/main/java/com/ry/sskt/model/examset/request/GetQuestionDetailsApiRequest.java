package com.ry.sskt.model.examset.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.common.request.RequestBase;
import lombok.Data;
import lombok.experimental.Accessors;

/// <summary>
/// 获取题目
/// </summary>
@Data
@Accessors(chain = true)
public class GetQuestionDetailsApiRequest extends RequestBase {
    /// <summary>
    /// 作业加密Id
    /// </summary>
    @JSONField(name = "ExamSetId")
    private String examSetId;

    /// <summary>
    /// 学生加密ID
    /// </summary>
    @JSONField(name = "StudentId")
    private String studentId;

}
