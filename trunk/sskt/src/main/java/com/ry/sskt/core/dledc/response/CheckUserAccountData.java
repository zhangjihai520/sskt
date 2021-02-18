package com.ry.sskt.core.dledc.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.core.dledc.DledcResponse;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CheckUserAccountData {
    /// <summary>
    /// 用户信息
    /// </summary>
    private CheckUserAccountUserInfo SingleData;
}
