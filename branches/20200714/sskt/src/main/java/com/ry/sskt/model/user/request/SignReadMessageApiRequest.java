package com.ry.sskt.model.user.request;

import com.ry.sskt.model.common.request.RequestBase;
import com.ry.sskt.model.common.request.RequestPageBase;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 【消息】标记已读消息Request
 */
@Data
@Accessors(chain = true)
public class SignReadMessageApiRequest extends RequestBase {
    /// <summary>
    /// 消息ID，加密
    /// </summary>
    private String MessageId;

    /// <summary>
    /// 消息状态，0未读、1已读
    /// </summary>
    private int StatusFlag;
}
