package com.ry.sskt.model.discuss.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.common.request.RequestBase;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SendMessageApiRequest extends RequestBase {

    /**
     * 发送至用户ID
     */
    @JSONField(name = "ToUserId")
    private int toUserId;

    /// <summary>
    /// 课程Id
    /// </summary>
    @JSONField(name = "SubjectId")
    private String subjectId;

    /// <summary>
    /// 房间状态  0正常   1禁言  (0正常，1、白名单，2、黑名单)  [0未开放  1开放]
    /// </summary>
    @JSONField(name = "Status")
    private int status;

    /// <summary>
    /// 消息内容
    /// </summary>
    @JSONField(name = "MessageContent")
    private String messageContent;
}
