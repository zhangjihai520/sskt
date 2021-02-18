package com.ry.sskt.service.impl;

import com.ry.sskt.core.utils.RedisUtil;
import com.ry.sskt.mapper.TalkMessageMapper;
import com.ry.sskt.model.common.constant.CommonConst;
import com.ry.sskt.model.common.constant.UserTypeEnum;
import com.ry.sskt.model.discuss.entity.TalkMessage;
import com.ry.sskt.model.discuss.entity.cache.SubjectRoomUserOnlineCouchBaseObject;
import com.ry.sskt.model.discuss.entity.cache.TalkMessageCouchBaseObject;
import com.ry.sskt.model.discuss.entity.cache.UserSubjectRoomCouchBaseObject;
import com.ry.sskt.model.subject.entity.SubjectRoom;
import com.ry.sskt.service.ISubjectRoomService;
import com.ry.sskt.service.ITalkMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.List;

@Service
public class TalkMessageService implements ITalkMessageService {
    @Autowired
    ISubjectRoomService subjectRoomService;
    @Autowired
    TalkMessageMapper talkMessageMapper;

    @Override
    public void sendMessage(TalkMessage message) {
        int talkMessageId = talkMessageMapper.insertTalkMessage(message);
        message.setTalkMessageId(talkMessageId);
        TalkMessageCouchBaseObject obj = new TalkMessageCouchBaseObject();
        obj.setSubjectId(message.getSubjectId());
        obj.setSubjectRoomId(message.getSubjectRoomId());
        obj.getMessageList().add(message);
        RedisUtil.updateCas(obj, CommonConst.MINUTE_360);
    }

    @Override
    public int getTalkMessageSubjectRoomId(int userId, int userTypeId, int subjectId) {
        UserSubjectRoomCouchBaseObject cache = new UserSubjectRoomCouchBaseObject(userId, subjectId);
        cache = RedisUtil.getObj(cache.getKey(), UserSubjectRoomCouchBaseObject.class);
        if (cache != null) {
            return cache.getSubjectRoomId();
        }
        cache = getUserSubjectRoomInfoFromDb(userId, userTypeId, subjectId);
        if (cache != null) {
            RedisUtil.setObj(cache, CommonConst.MINUTE_360);
            return cache.getSubjectRoomId();
        }
        return 0;
    }

    @Override
    public List<TalkMessage> getMessage(int subjectId, int subjectRoomId) {
        TalkMessageCouchBaseObject cache = new TalkMessageCouchBaseObject(subjectId, subjectRoomId);
        cache = RedisUtil.getObj(cache.getKey(), TalkMessageCouchBaseObject.class);
        if (cache == null) {
            return new LinkedList<>();
        }
        return cache.getMessageList();
    }
    @Override
    public void setUserOnline(int userId, int subjectRoomId, boolean online) {
        SubjectRoomUserOnlineCouchBaseObject obj = new SubjectRoomUserOnlineCouchBaseObject();
        obj.setSubjectRoomId(subjectRoomId);
        obj.getUserDictionary().put(userId, online);
        RedisUtil.updateCas(obj, CommonConst.MINUTE_1440);
    }

    @Override
    public HashMap<Integer, Boolean> getUserOnlineStatus(int subjectRoomId) {
        SubjectRoomUserOnlineCouchBaseObject cache = new SubjectRoomUserOnlineCouchBaseObject(subjectRoomId);
        cache = RedisUtil.getObj(cache.getKey(), SubjectRoomUserOnlineCouchBaseObject.class);
        if (cache == null) {
            return new HashMap<Integer, Boolean>();
        }
        return cache.getUserDictionary();
    }

    /// <summary>
    /// 从数据库中获取用户所在教室的信息
    /// </summary>
    /// <param name="userId"></param>
    /// <param name="userTypeId"></param>
    /// <param name="subjectId"></param>
    /// <returns></returns>
    private UserSubjectRoomCouchBaseObject getUserSubjectRoomInfoFromDb(int userId, int userTypeId, int subjectId) {
        SubjectRoom room = null;
        if (userTypeId == UserTypeEnum.Student.getCode()) {
            room = subjectRoomService.getSubjectRoomByStudentId(subjectId, userId);
        } else if (userTypeId == UserTypeEnum.Teacher.getCode()) {
            room = subjectRoomService.getSubjectRoomList(subjectId).stream().filter(m -> m.getTeacherId() == userId).findFirst().orElse(null);
            if (room == null) {
                room = subjectRoomService.getSubjectRoomByTeacherId(subjectId, userId);
            }
        }
        if (room == null) return null;
        return new UserSubjectRoomCouchBaseObject(userId, room.getSubjectId(), room.getSubjectRoomId());
    }
}
