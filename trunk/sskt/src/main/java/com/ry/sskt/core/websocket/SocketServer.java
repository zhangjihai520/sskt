package com.ry.sskt.core.websocket;

import com.alibaba.fastjson.JSONObject;
import com.ry.sskt.MessageConsumer;
import com.ry.sskt.controller.action.IDiscussActionService;
import com.ry.sskt.core.utils.RedisUtil;
import com.ry.sskt.model.common.entity.User;
import com.ry.sskt.model.discuss.entity.cache.UserSubjectRoomCouchBaseObject;
import com.ry.sskt.model.discuss.response.SocketTalkMessage;
import com.ry.sskt.service.ISubjectRoomService;
import com.ry.sskt.service.ITalkMessageService;
import com.ry.sskt.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
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
    //  这里使用静态，让 service 属于类
    private static IDiscussActionService discussActionService;

    private static String SESSION_H5_KEY_FORMAT = "%s_h5";
    private static String SESSION_WEB_KEY_FORMAT = "%s_web";

    // 注入的时候，给类的 service 注入
    @Autowired
    public void setTalkMessageService(ITalkMessageService talkMessageService) {
        SocketServer.talkMessageService = talkMessageService;
    }

    // 注入的时候，给类的 service 注入
    @Autowired
    public void setDiscussActionService(IDiscussActionService discussActionService) {
        SocketServer.discussActionService = discussActionService;
    }

    private static final Map<String, Map<String, Session>> rooms = new ConcurrentHashMap();

    @OnOpen
    public void open(@PathParam(value = "channel") String channel, @PathParam(value = "userid") String userid, Session session) {
        if (!this.rooms.containsKey(channel)) {
            Map<String, Session> room = new ConcurrentHashMap<>();
            room.put(userid, session);
            this.rooms.put(channel, room);
            new Thread(new RedisSubscriber(channel)).start();
            new Thread(new MessageConsumer(Integer.parseInt(channel),talkMessageService)).start();
        } else {
            this.rooms.get(channel).put(userid, session);
        }
        var uid = userid.split("_")[0];
        talkMessageService.setUserOnline(Integer.parseInt(uid), Integer.parseInt(channel), true);
        var onLineUsers = discussActionService.GenerateUserOnlineInfos(Integer.parseInt(channel));
        var socketTalkMessage = new SocketTalkMessage().setUserInfoList(onLineUsers).setMessageType(1);
        RedisUtil.publish(channel + "", JSONObject.toJSONString(socketTalkMessage));
    }

    @OnMessage
    public void onMessage(@PathParam(value = "channel") String channel, @PathParam(value = "userid") String userid, String message, Session session) {
        //RedisUtil.publish(channel, message);
        sendMessage(message, session);
        var uid = userid.split("_")[0];
        talkMessageService.setUserOnline(Integer.parseInt(uid), Integer.parseInt(channel), true);
    }

    @OnClose
    public void onClose(@PathParam(value = "channel") String channel, @PathParam(value = "userid") String userid, Session session) {
        rooms.get(channel).remove(session);
        var uid = userid.split("_")[0];
        talkMessageService.setUserOnline(Integer.parseInt(uid), Integer.parseInt(channel), false);
        var onLineUsers = discussActionService.GenerateUserOnlineInfos(Integer.parseInt(channel));
        var socketTalkMessage = new SocketTalkMessage().setUserInfoList(onLineUsers).setMessageType(1);
        RedisUtil.publish(channel + "", JSONObject.toJSONString(socketTalkMessage));
    }

    //连接异常
    @OnError
    public void onError(Session session, Throwable error) {
    }

    //自定义发送消息
    public static void sendMessage(String message, Session session) {
        if (session != null) {
            try {
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //自定义发送消息
    public static void sendMessage(String channel, String message, String userId) {
        if (rooms.containsKey(channel)) {
            Map<String, Session> map = rooms.get(channel);
            for (Map.Entry<String, Session> sessionMap : map.entrySet()) {
                var key = sessionMap.getKey();
                if (key.equalsIgnoreCase(String.format(SESSION_H5_KEY_FORMAT,userId)) || key.equalsIgnoreCase(String.format(SESSION_WEB_KEY_FORMAT,userId))) {
                    var session = sessionMap.getValue();
                    sendMessage(message, session);
                }
            }
        }
    }

    //全部发送
    public static void sendAll(String channel, String msg) {
        if (rooms.containsKey(channel)) {
            Map<String, Session> map = rooms.get(channel);
            for (Session session : map.values()) {
                sendMessage(msg, session);
            }
        }
    }
}