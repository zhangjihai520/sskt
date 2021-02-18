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
public class GetAnswersStatisticsApiRequest extends RequestBase {
    /// <summary>
    /// 作业加密Id
    /// </summary>
    @JSONField(name = "ExamSetId")
    private String examSetId;

}
