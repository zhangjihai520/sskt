package com.ry.sskt.model.user.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.common.request.RequestBase;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 登录Request
 */
@Data
@Accessors(chain = true)
public class LoginApiRequest extends RequestBase {

    /// <summary>
    /// 账号
    /// </summary>
    @JSONField(name = "UserName")
    private String userName;

    /// <summary>
    /// 密码
    /// </summary>
    @JSONField(name = "Password")
    private String password;
}
