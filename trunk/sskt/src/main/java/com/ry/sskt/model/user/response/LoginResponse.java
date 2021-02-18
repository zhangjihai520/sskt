package com.ry.sskt.model.user.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginResponse {

    /// <summary>
    /// 登录状态，1成功 其他失败
    /// </summary>
    @JSONField(name = "LoginStaus")
    private int loginStaus;

    /// <summary>
    /// 用户token
    /// </summary>
    @JSONField(name = "Token")
    private String token;

    /// <summary>
    /// UId
    /// </summary>
    @JSONField(name = "UId")
    private String uId;

    /// <summary>
    /// 用户名
    /// </summary>
    @JSONField(name = "UserName")
    private String userName;
}
