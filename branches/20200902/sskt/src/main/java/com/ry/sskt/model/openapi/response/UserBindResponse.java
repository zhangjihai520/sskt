package com.ry.sskt.model.openapi.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;
/// <summary>
/// 注册用户返回对象
/// </summary>
@Data
@Accessors(chain = true)
public class UserBindResponse {
    /// <summary>
    /// 会话标识
    /// </summary>
    @JSONField(name="SessionId")
    private String sessionId;

    /// <summary>
    /// 标识有效时长，单位秒
    /// </summary>
    @JSONField(name="ExpireAt")
    private int expireAt;

    /// <summary>
    /// 魔题库用户唯一标识
    /// </summary>
    @JSONField(name="UserIndicateId")
    private String userIndicateId;
}
