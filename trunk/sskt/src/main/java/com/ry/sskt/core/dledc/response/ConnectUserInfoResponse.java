package com.ry.sskt.core.dledc.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.core.dledc.DledcResponse;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// <summary>
/// 通过access_token拉取用户信息Response
/// </summary>
@Data
@Accessors(chain = true)
public class ConnectUserInfoResponse extends DledcResponse {
    /// <summary>
    /// userId
    /// </summary>
    @JSONField(name = "sub")
    private String sub;

    /// <summary>
    /// preferred_username
    /// </summary>
    @JSONField(name = "preferred_username")
    private String preferred_username;

    /// <summary>
    /// role
    /// </summary>
    @JSONField(name = "role")
    private List<String> role;
}
