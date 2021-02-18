package com.ry.sskt.model.teacher.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.common.request.RequestBase;
import com.ry.sskt.model.common.request.RequestPageBase;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 【消息】教师、学生消息列表Request
 */
@Data
@Accessors(chain = true)
public class GetMessageListApiRequest extends RequestPageBase {
    /// <summary>
    /// -1所有0未读1已读
    /// </summary>
    private int MessageTypeId;

}
