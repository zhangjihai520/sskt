package com.ry.sskt.service;

import com.ry.sskt.model.discuss.entity.TalkMessage;
import com.ry.sskt.model.subject.entity.view.SubjectListByFilterView;
import javafx.collections.transformation.SortedList;

import java.util.*;

/**
 * 聊天消息接口
 */
public interface ITalkMessageService {

    /// <summary>
    /// 发送普通信息
    /// </summary>
    /// <param name="message"></param>
    void sendMessage(TalkMessage message);

    /// <summary>
    /// 优先从缓存中获取当前用户所在的讨论区的房间
    /// </summary>
    /// <returns></returns>
    int getTalkMessageSubjectRoomId(int userId, int userTypeId, int subjectId);

    /// <summary>
    /// 获取讨论区全部聊天记录
    /// </summary>
    /// <param name="subjectId"></param>
    /// <param name="subjectRoomId"></param>
    /// <returns></returns>
    List<TalkMessage> getMessage(int subjectId, int subjectRoomId);

    /// <summary>
    /// 设置用户在线
    /// </summary>
    /// <returns></returns>
    void setUserOnline(int userId, int subjectRoomId, boolean online);


    HashMap<Integer, Boolean> getUserOnlineStatus(int subjectRoomId);
}
