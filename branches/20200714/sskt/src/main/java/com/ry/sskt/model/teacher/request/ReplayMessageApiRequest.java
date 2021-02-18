package com.ry.sskt.model.teacher.request;

import com.ry.sskt.model.common.request.RequestBase;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 【教学管理】回复消息Request
 */
@Data
@Accessors(chain = true)
public class ReplayMessageApiRequest extends RequestBase {

    /// <summary>
    /// 回复消息ID，加密
    /// </summary>
    private String MessageId;

    /// <summary>
    /// 回复对象用户id，加密
    /// </summary>
    private String TargetUserId;

    /// <summary>
    /// 回复消息内容
    /// </summary>
    private String Content;

}
