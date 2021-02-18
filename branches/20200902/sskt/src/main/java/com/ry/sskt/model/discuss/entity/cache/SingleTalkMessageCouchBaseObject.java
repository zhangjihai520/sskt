package com.ry.sskt.model.discuss.entity.cache;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.core.annotation.IRedisOperationObject;
import com.ry.sskt.core.annotation.IRedisStoredObject;
import com.ry.sskt.core.utils.ProtoBufUtil;
import com.ry.sskt.model.discuss.entity.TalkMessage;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.collections.CollectionUtils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Data
@Accessors(chain = true)
public class SingleTalkMessageCouchBaseObject implements IRedisStoredObject, IRedisOperationObject {
    /// <summary>
    /// 教室Id
    /// </summary>
    @JSONField(name = "SubjectRoomId")
    private int subjectRoomId;

    /// <summary>
    /// 聊天室状态  0在线聊天  1在线禁言
    /// </summary>
    @JSONField(name = "userId")
    private int userId;

    /// <summary>
    /// 信息列表
    /// </summary>
    @JSONField(name = "MessageList")
    private List<TalkMessage> messageList;

    /**
     * 最后一次显示时间，仿照微信
     */
    @JSONField(name = "LastShowTimeZone")
    private long lastShowTimeZone;

    public void add(TalkMessage messageObject) {
        if (CollectionUtils.isEmpty(this.messageList)) {
            this.messageList = new LinkedList<TalkMessage>();
        } else {
            this.messageList.add(messageObject);
        }
    }

    private static String key = "TalkMessage:SubjectRoomId_%s";

    @Override
    public String getKey() {
        return String.format(key, this.subjectRoomId);
    }

    public SingleTalkMessageCouchBaseObject(int subjectRoomId) {
        this.subjectRoomId = subjectRoomId;
        this.messageList = new LinkedList<TalkMessage>();
    }

    public SingleTalkMessageCouchBaseObject() {
        this.messageList = new LinkedList<TalkMessage>();
    }

    @Override
    public boolean operation(byte[] objByte) throws IOException {
        if (CollectionUtils.isEmpty(this.messageList)) {
            this.messageList = new LinkedList<TalkMessage>();
        }
        if (objByte != null) {
            SingleTalkMessageCouchBaseObject obj = ProtoBufUtil.deserializer(objByte, SingleTalkMessageCouchBaseObject.class);
            if (CollectionUtils.isNotEmpty(obj.getMessageList())) {
                this.messageList.addAll(obj.getMessageList());
            }
        }
        return true;
    }
}
