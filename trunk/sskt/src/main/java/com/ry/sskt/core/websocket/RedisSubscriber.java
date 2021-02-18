package com.ry.sskt.core.websocket;

import com.alibaba.fastjson.JSON;
import com.ry.sskt.core.utils.RedisUtil;
import com.ry.sskt.model.discuss.response.SocketTalkMessage;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPubSub;

import java.util.concurrent.Executors;

//@Component
@Slf4j
public class RedisSubscriber extends JedisPubSub implements Runnable {
    private String channel;

    public RedisSubscriber(String channel) {
        this.channel = channel;
    }

    //监听到订阅频道接受到消息时的回调
    @Override
    public void onMessage(String channel, String message) {
        try {
            var socketMessage = JSON.parseObject(message, SocketTalkMessage.class);
            if (null != socketMessage) {
                var messageObj = socketMessage.getTalkMessage();
                if (messageObj.getToUserId() > 0) {
                    SocketServer.sendMessage(channel, message, messageObj.getToUserId() + "");
                    SocketServer.sendMessage(channel, message, messageObj.getUserId() + "");
                    return;
                }
            }
            SocketServer.sendAll(channel, message);
        } catch (Exception e) {
            SocketServer.sendAll(channel, message);
        }
    }

    //监听到订阅模式接受到消息时的回调
    @Override
    public void onPMessage(String pattern, String channel, String message) {

    }

    //订阅了频道会调用
    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        log.info("我订阅了{}频道", channel);
    }

    //取消订阅 会调用
    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {

    }

    //取消订阅模式时的回调
    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {

    }

    //  订阅频道模式时的回调
    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {

    }

    public void run() {
        //开一个线程去订阅,不然会占用主线程运行
        try {
            RedisUtil.subscribe(this, channel);
        } catch (Exception e) {
            System.out.println("订阅失败");
        }

    }
}