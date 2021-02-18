package com.ry.sskt.model.discuss.entity.cache;

import com.alibaba.fastjson.annotation.JSONField;
import com.couchbase.client.java.document.AbstractDocument;
import com.ry.sskt.core.annotation.IRedisOperationObject;
import com.ry.sskt.core.annotation.IRedisStoredObject;
import com.ry.sskt.core.utils.GZipUtil;
import com.ry.sskt.core.utils.ProtoBufUtil;
import com.ry.sskt.model.discuss.entity.TalkMessage;
import io.protostuff.Tag;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.util.*;

@Data
@Accessors(chain = true)
public class TalkMessageCouchBaseObject implements IRedisStoredObject, IRedisOperationObject {
    /// <summary>
    /// 课程Id
    /// </summary>

    @JSONField(name = "SubjectId")
    private int subjectId;

    /// <summary>
    /// 教室Id
    /// </summary>

    @JSONField(name = "SubjectRoomId")
    private int subjectRoomId;
    /// <summary>
    /// 信息列表
    /// </summary>

    @JSONField(name = "MessageList")
    private List<TalkMessage> messageList;


    public void add(TalkMessage messageObject) {
        if (messageObject == null) {
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

    public TalkMessageCouchBaseObject() {
        this.messageList = new LinkedList<TalkMessage>();
    }

    public TalkMessageCouchBaseObject(Integer subjectId, int subjectRoomId) {
        this.subjectId = subjectId;
        this.subjectRoomId = subjectRoomId;
    }

    @Override
    public boolean operation(byte[] objByte) throws IOException {
        if (objByte != null) {
            TalkMessageCouchBaseObject obj = ProtoBufUtil.deserializer(objByte, TalkMessageCouchBaseObject.class);
            this.messageList.addAll(obj.getMessageList());
        }
        return true;
    }
}
