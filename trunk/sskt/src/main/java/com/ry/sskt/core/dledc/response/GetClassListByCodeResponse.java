package com.ry.sskt.core.dledc.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.core.dledc.DledcResponse;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GetClassListByCodeResponse extends DledcResponse {
    /// <summary>
    /// 处理状态
    /// </summary>
    @JSONField(name = "StatusCode")
    private int statusCode;
    /// <summary>
    /// 处理结果信息
    /// </summary>
    @JSONField(name = "Message")
    private String message;
    /// <summary>
    /// 返回参数
    /// </summary>
    @JSONField(name = "Data")
    private GetClassListByCodeResponseData data;
}
