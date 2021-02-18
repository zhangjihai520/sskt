package com.ry.sskt.core.websocket;

import com.ry.sskt.service.ITalkMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/chat/{channel}/{userid}")
@Component
@Slf4j
public class SocketServer {
    //  这里使用静态，让 service 属于类
    private static ITalkMessageService talkMessageService;

    // 注入的时候，给类的 service 注入
    @Autowired
    public void setTalkMessageService(ITalkMessageService talkMessageService) {
        SocketServer.talkMessageService = talkMessageService;
    }

    private static final Map<String, Set<Session>> rooms = new ConcurrentHashMap();

    @OnOpen
    public void open(@PathParam(value = "channel") String channel, @PathParam(value = "userid") String userid, Session session) {
        if (!this.rooms.containsKey(channel)) {
            Set<Session> room = new HashSet<Session>();
            room.add(session);
            this.rooms.put(channel, room);
            new Thread(new RedisSubscriber(channel)).start();
        } else {
            this.rooms.get(channel).add(session);
        }
        talkMessageService.setUserOnline(Integer.parseInt(userid), Integer.parseInt(channel), true);

    }

    @OnMessage
    public void onMessage(@PathParam(value = "channel") String channel, @PathParam(value = "userid") String userid, String message, Session session) {
        //RedisUtil.publish(channel, message);
        sendMessage(message, session);
        talkMessageService.setUserOnline(Integer.parseInt(userid), Integer.parseInt(channel), true);
    }

    @OnClose
    public void onClose(@PathParam(value = "channel") String channel, @PathParam(value = "userid") String userid, Session session) {
        rooms.get(channel).remove(session);
        talkMessageService.setUserOnline(Integer.parseInt(userid), Integer.parseInt(channel), false);
    }

    //连接异常
    @OnError
    public void onError(Session session, Throwable error) {
    }

    //自定义发送消息
    public static void sendMessage(String message, Session session) {
        if (session != null) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //全部发送
    public static void sendAll(String channel, String msg) {
        if (rooms.containsKey(channel)) {
            for (Session session : rooms.get(channel)) {
                sendMessage(msg, session);
            }
        }
    }
}