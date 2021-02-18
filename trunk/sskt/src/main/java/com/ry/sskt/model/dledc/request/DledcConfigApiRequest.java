package com.ry.sskt.model.dledc.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.common.request.RequestBase;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 获取教辅平台对接参数Request
 */
@Data
@Accessors(chain = true)
public class DledcConfigApiRequest extends RequestBase {
    /// <summary>
    /// 云平台通过回调地址传来的code
    /// </summary>
    @JSONField(name = "AccessTypeId")
    private int accessTypeId;

}
