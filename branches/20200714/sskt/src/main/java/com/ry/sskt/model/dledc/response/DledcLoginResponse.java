package com.ry.sskt.model.dledc.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DledcLoginResponse {

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

    public DledcLoginResponse() {
    }

    public DledcLoginResponse(int loginStaus, String token) {
        this.loginStaus = loginStaus;
        this.token = token;
    }
}

