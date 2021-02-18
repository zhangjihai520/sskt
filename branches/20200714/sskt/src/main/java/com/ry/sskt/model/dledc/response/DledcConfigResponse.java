package com.ry.sskt.model.dledc.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 获取教辅平台对接参数Response
 */
@Data
@Accessors(chain = true)
public class DledcConfigResponse {

    /// <summary>
    /// Api接口地址
    /// </summary>
    /// <returns></returns>
    @JSONField(name = "DledcApiUrl")
    private String dledcApiUrl;

    /// <summary>
    /// 登录回调地址
    /// </summary>
    /// <returns>异步通知地址</returns>
    @JSONField(name = "RedirectUri")
    private String redirectUri;

    /// <summary>
    /// ClientId
    /// </summary>
    /// <returns></returns>
    @JSONField(name = "ClientId")
    private String clientId;

    /// <summary>
    /// ClientSecret
    /// </summary>
    /// <returns></returns>
    @JSONField(name = "ClientSecret")
    private String clientSecret;

    /// <summary>
    /// 统一退出回调地址。
    /// </summary>
    /// <returns></returns>
    @JSONField(name = "PostLogoutRedirectUri")
    private String postLogoutRedirectUri;

    /// <summary>
    /// 教辅平台首页
    /// </summary>
    @JSONField(name = "DledcPlatformUrl")
    private String dledcPlatformUrl;
}

