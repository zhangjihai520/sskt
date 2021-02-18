package com.ry.sskt.model.discuss.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.common.request.RequestBase;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TalkMessageIdApiRequest extends RequestBase {
    /**
     * 发送至用户ID
     */
    @JSONField(name = "ToUserId")
    private int toUserId;
    /**
     * 消息ID
     */
    @JSONField(name = "TalkMessageId")
    private int talkMessageId;

    /**
     * 房间号
     */
    @JSONField(name = "SubjectRoomId")
    private int subjectRoomId;

}
