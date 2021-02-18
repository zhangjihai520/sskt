package com.ry.sskt.service;

import com.ry.sskt.model.discuss.entity.TalkMessage;
import com.ry.sskt.model.discuss.entity.cache.TalkMessageCouchBaseObject;
import com.ry.sskt.model.discuss.entity.cache.UserSubjectRoomCouchBaseObject;
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
    UserSubjectRoomCouchBaseObject getUserSubjectRoomInfo(int userId, int userTypeId, int subjectId);

    /// <summary>
    /// 获取讨论区全部聊天记录
    /// </summary>
    /// <param name="subjectId"></param>
    /// <param name="subjectRoomId"></param>
    /// <returns></returns>
    TalkMessageCouchBaseObject getMessageCache(int subjectRoomId);


    /// <summary>
    /// 设置房间互动状态（正常、禁言）
    /// </summary>
    /// <returns></returns>
    void setRoomStatus(int subjectRoomId,int userid, int status);

    /// <summary>
    /// 设置房间互动状态（正常、禁言）
    /// </summary>
    /// <returns></returns>
    void setUserStatus(int userId, int subjectRoomId, int status);

    /// <summary>
    /// 设置用户在线
    /// </summary>
    /// <returns></returns>
    void setUserOnline(int userId, int subjectRoomId, boolean online);

    HashMap<Integer, Integer> getUserStatus(int subjectRoomId);

    HashMap<Integer, Boolean> getUserOnlineStatus(int subjectRoomId);
}
