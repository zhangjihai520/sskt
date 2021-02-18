package com.ry.sskt;

import com.alibaba.fastjson.JSONObject;
import com.ry.sskt.core.utils.RedisUtil;
import com.ry.sskt.model.discuss.entity.TalkMessage;
import com.ry.sskt.service.ITalkMessageService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class MessageConsumer implements Runnable {
    private static long towHourTimeMillisZone = 2 * 60 * 60 * 1000;
    private static String key = "MessageQueue:SubjectRoomId_%s";
    public volatile long startTimeMillisZone = 0;
    private int subjectRoomId;
    private ITalkMessageService talkMessageService;

    public MessageConsumer() {
    }

    public MessageConsumer(int subjectRoomId, ITalkMessageService talkMessageService) {
        this.subjectRoomId = subjectRoomId;
        this.talkMessageService = talkMessageService;
    }

    public String getKey() {
        return String.format(key, this.subjectRoomId);
    }

    public void consumerMessage() {
        List<String> brpop = RedisUtil.brpop(0, getKey());//0是timeout,返回的是一个集合，第一个是消息的key，第二个是消息的内容
        talkMessageService.processMessage(JSONObject.parseObject(brpop.get(1), TalkMessage.class));
    }

    @Override
    public void run() {
        startTimeMillisZone = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTimeMillisZone < towHourTimeMillisZone) {
            try {
                consumerMessage();
            } catch (Exception e) {
                log.error("聊天消息队列消费失败", e);
                e.printStackTrace();
            }

        }
    }
}
