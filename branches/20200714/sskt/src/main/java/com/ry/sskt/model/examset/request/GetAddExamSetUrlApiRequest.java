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
public class GetAddExamSetUrlApiRequest extends RequestBase {
    /// <summary>
    /// 课程加密ID
    /// </summary>
    @JSONField(name = "SubjectId")
    private String subjectId;

    /// <summary>
    /// 学科ID
    /// </summary>
    @JSONField(name = "CourseId")
    private int courseId;

    /// <summary>
    /// 1线上 2线下
    /// </summary>
    @JSONField(name = "Online")
    private int online;

}
