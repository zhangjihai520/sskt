package com.ry.sskt;

import com.ry.sskt.core.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageProducer {
    private static String key = "MessageQueue:SubjectRoomId_%s";
    private int subjectRoomId;
    public MessageProducer() {
    }
    public MessageProducer(int subjectRoomId) {
        this.subjectRoomId = subjectRoomId;
    }

    public String getKey() {
        return String.format(key, this.subjectRoomId);
    }

    public void putMessage(String message) {
        RedisUtil.lPush(getKey(), message);
    }
}
