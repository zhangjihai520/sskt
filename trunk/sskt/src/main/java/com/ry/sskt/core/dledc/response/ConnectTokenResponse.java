package com.ry.sskt.core.dledc.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.core.dledc.DledcResponse;
import lombok.Data;
import lombok.experimental.Accessors;

/// <summary>
/// 通过机构code获取用户列表信息
/// </summary>
@Data
@Accessors(chain = true)
public class ConnectTokenResponse extends DledcResponse {
    /// <summary>
    /// token
    /// </summary>
    @JSONField(name = "access_token")
    private String access_token;

    /// <summary>
    /// expires_in
    /// </summary>
    @JSONField(name = "expires_in")
    private int expires_in;

    /// <summary>
    /// id_token，统一退出接口使用
    /// </summary>
    @JSONField(name = "id_token")
    private String id_token;

    /// <summary>
    /// scope
    /// </summary>
    @JSONField(name = "scope")
    private String scope;

    /// <summary>
    /// session_key
    /// </summary>
    @JSONField(name = "session_key")
    private String session_key;

    /// <summary>
    /// session_secret
    /// </summary>
    @JSONField(name = "session_secret")
    private String session_secret;

    /// <summary>
    /// token_type
    /// </summary>
    @JSONField(name = "token_type")
    private String token_type;
}
