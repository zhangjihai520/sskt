package com.ry.sskt.model.examset.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.common.request.RequestBase;
import lombok.Data;
import lombok.experimental.Accessors;

/// <summary>
/// 【课堂作业】获取布置新题地址 请求产生
/// </summary>
@Data
@Accessors(chain = true)
public class SaveExamSetApiRequest extends RequestBase {
    /// <summary>
    /// 作业加密Id
    /// </summary>
    @JSONField(name = "ExamSetId")
    private String examSetId;
    /// <summary>
    /// 作业名称
    /// </summary>
    @JSONField(name = "ExamSetName")
    private String examSetName;

    /// <summary>
    /// 年级Id
    /// </summary>
    @JSONField(name = "GradeId")
    private int gradeId;

    /// <summary>
    /// 作业类型，1课前作业，2随堂练习，3课后作业
    /// </summary>
    @JSONField(name = "ExamSetTypeId")
    private int examSetTypeId;
}
