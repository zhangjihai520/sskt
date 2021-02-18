package com.ry.sskt.core.dledc.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GetUserBaseInfoResponseData {
    /// <summary>
    /// 机构列表
    /// </summary>
    @JSONField(name = "SingleData")
    private UserBaseInfo singleData;
}
