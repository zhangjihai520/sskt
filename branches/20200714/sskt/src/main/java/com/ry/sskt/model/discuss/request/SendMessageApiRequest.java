package com.ry.sskt.model.discuss.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.common.request.RequestBase;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SendMessageApiRequest extends RequestBase {

    /// <summary>
    /// 课程Id
    /// </summary>
    @JSONField(name = "SubjectId")
    private String subjectId;

    /// <summary>
    /// 消息内容
    /// </summary>
    @JSONField(name = "MessageContent")
    private String messageContent;
}
