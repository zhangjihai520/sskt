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
public class GetExamSetInfoApiRequest extends RequestBase {

    /// <summary>
    /// MOTK返回的题集Id teid
    /// </summary>
    @JSONField(name = "teid")
    private String teid;

    /// <summary>
    /// MOTK返回带回的参数，值为：课程ID_学科id
    /// </summary>
    @JSONField(name = "backurl")
    private String backurl;

}
