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
import org.apache.commons.collections.CollectionUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@Data
@Accessors(chain = true)
public class TalkMessageCouchBaseObject implements IRedisStoredObject, IRedisOperationObject {
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

    /// <summary>
    /// 聊天室状态  0在线聊天  1在线禁言
    /// </summary>
    @JSONField(name = "RoomStatus")
    private int roomStatus;

    /// <summary>
    /// 随堂作业开放状态  0关闭  1开放
    /// </summary>
    @JSONField(name = "ExamSetStatus")
    private int examSetStatus;

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

    public TalkMessageCouchBaseObject(int subjectRoomId) {
        this.subjectRoomId = subjectRoomId;
        this.messageList = new LinkedList<TalkMessage>();
    }

    public TalkMessageCouchBaseObject() {
        this.messageList = new LinkedList<TalkMessage>();
    }

    @Override
    public boolean operation(byte[] objByte) throws IOException {
        if (CollectionUtils.isEmpty(this.messageList)) {
            this.messageList = new LinkedList<TalkMessage>();
        }
        if (objByte != null) {
            TalkMessageCouchBaseObject obj = ProtoBufUtil.deserializer(objByte, TalkMessageCouchBaseObject.class);
            if (CollectionUtils.isNotEmpty(obj.getMessageList())) {
                this.messageList.addAll(obj.getMessageList());
                this.messageList = this.messageList.stream().collect(
                        collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getTalkMessageId()))),
                                LinkedList::new));
            }
        }
        return true;
    }
}
