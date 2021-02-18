package com.ry.sskt.model.examset.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.common.request.RequestBase;
import lombok.Data;
import lombok.experimental.Accessors;

/// <summary>
/// 作业上架请求参数
/// </summary>
@Data
@Accessors(chain = true)
public class SetExamSetOnlineApiRequest extends RequestBase {
    /// <summary>
    /// 作业加密Id
    /// </summary>
    @JSONField(name = "ExamSetId")
    private String examSetId;

    /// <summary>
    /// 1下架 2上架
    /// </summary>
    @JSONField(name = "StatusFlag")
    private int statusFlag;

    /// <summary>
    /// 课程加密Id
    /// </summary>
    @JSONField(name = "SubjectId")
    private String subjectId;

    /// <summary>
    /// 作业状态 作业类型，1课前作业，2随堂练习，3课后作业
    /// </summary>
    @JSONField(name = "ExamSetTypeId")
    private int examSetTypeId;

}
