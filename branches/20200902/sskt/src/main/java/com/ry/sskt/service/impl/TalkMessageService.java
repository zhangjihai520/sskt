package com.ry.sskt.service.impl;

import com.ry.sskt.core.utils.RedisUtil;
import com.ry.sskt.mapper.TalkMessageMapper;
import com.ry.sskt.model.common.constant.CommonConst;
import com.ry.sskt.model.common.constant.UserTypeEnum;
import com.ry.sskt.model.common.entity.User;
import com.ry.sskt.model.discuss.entity.TalkMessage;
import com.ry.sskt.model.discuss.entity.cache.SubjectRoomUserOnlineCouchBaseObject;
import com.ry.sskt.model.discuss.entity.cache.SubjectRoomUserStatusCouchBaseObject;
import com.ry.sskt.model.discuss.entity.cache.TalkMessageCouchBaseObject;
import com.ry.sskt.model.discuss.entity.cache.UserSubjectRoomCouchBaseObject;
import com.ry.sskt.model.subject.entity.SubjectRoom;
import com.ry.sskt.service.ISubjectRoomService;
import com.ry.sskt.service.ISubjectRoomStudentService;
import com.ry.sskt.service.ISubjectService;
import com.ry.sskt.service.ITalkMessageService;
import lombok.var;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TalkMessageService implements ITalkMessageService {
    @Autowired
    ISubjectService subjectService;
    @Autowired
    ISubjectRoomService subjectRoomService;
    @Resource
    TalkMessageMapper talkMessageMapper;
    @Resource
    ISubjectRoomStudentService subjectRoomStudentService;

    @Override
    public void sendMessage(TalkMessage message) {
        int talkMessageId = talkMessageMapper.insertTalkMessage(message);
        message.setTalkMessageId(talkMessageId);
        var obj = new TalkMessageCouchBaseObject(message.getSubjectRoomId());
        var cache = RedisUtil.getObj(obj.getKey(), TalkMessageCouchBaseObject.class);
        Long nowSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        if (cache == null || nowSecond > cache.getLastShowTimeZone() + 300) {
            obj.setLastShowTimeZone(nowSecond);
            message.setShowDateTime(LocalDateTime.now());
        }else {
            obj.setLastShowTimeZone(cache.getLastShowTimeZone()).setRoomStatus(cache.getRoomStatus());
        }
        obj.getMessageList().add(message);
        RedisUtil.updateCas(obj);
    }

    @Override
    public UserSubjectRoomCouchBaseObject getUserSubjectRoomInfo(int userId, int userTypeId, int subjectId) {
        UserSubjectRoomCouchBaseObject cache = new UserSubjectRoomCouchBaseObject(userId, subjectId);
        cache = RedisUtil.getObj(cache.getKey(), UserSubjectRoomCouchBaseObject.class);
        if (cache != null) {
            return cache;
        }
        cache = getUserSubjectRoomInfoFromDb(userId, userTypeId, subjectId);
        if (cache != null) {
            RedisUtil.setObj(cache, CommonConst.MINUTE_1440);
            return cache;
        }
        return null;
    }

    @Override
    public TalkMessageCouchBaseObject getMessageCache(int subjectRoomId) {
        TalkMessageCouchBaseObject cache = new TalkMessageCouchBaseObject().setSubjectRoomId(subjectRoomId);
        cache = RedisUtil.getObj(cache.getKey(), TalkMessageCouchBaseObject.class);
        if (cache != null) {
            if (CollectionUtils.isEmpty(cache.getMessageList())) {
                cache.setMessageList(new LinkedList<>());
            }
            return cache;
        }
        return new TalkMessageCouchBaseObject().setSubjectRoomId(subjectRoomId);
    }

    @Override
    public void setUserOnline(int userId, int subjectRoomId, boolean online) {
        SubjectRoomUserOnlineCouchBaseObject obj = new SubjectRoomUserOnlineCouchBaseObject();
        obj.setSubjectRoomId(subjectRoomId);
        obj.getUserOnlineDictionary().put(userId, online);
        RedisUtil.updateCas(obj, CommonConst.MINUTE_1440);
    }

    @Override
    public void setRoomStatus(int subjectRoomId, int userId, int status) {
        TalkMessageCouchBaseObject cache = new TalkMessageCouchBaseObject().setSubjectRoomId(subjectRoomId).setRoomStatus(status);
        RedisUtil.updateCas(cache, CommonConst.MINUTE_1440);
        SubjectRoomUserStatusCouchBaseObject roomUserStatusCache = new SubjectRoomUserStatusCouchBaseObject(subjectRoomId);
        roomUserStatusCache = RedisUtil.getObj(roomUserStatusCache.getKey(), SubjectRoomUserStatusCouchBaseObject.class);
        HashMap<Integer, Integer> map = new HashMap();
        if (roomUserStatusCache != null) {
            map = roomUserStatusCache.getUserStatusDictionary();
        }
        List<Integer> blackUser = new LinkedList<>();
        if (!map.isEmpty()) {
            blackUser = map.entrySet().stream().filter(m -> m.getValue() == 2).map(x -> x.getKey()).collect(Collectors.toList());
        }
        List<User> allUser = subjectRoomStudentService.GetAllBySubjectRoomId(subjectRoomId);
        for (User user : allUser) {
            if (blackUser.contains(user.getUserId())) continue;
            if (user.getUserId() == userId || user.getRoomUserType() == 3) {
                map.put(user.getUserId(), 0);
            } else {
                map.put(user.getUserId(), status);
            }
        }
        roomUserStatusCache = new SubjectRoomUserStatusCouchBaseObject(subjectRoomId).setUserStatusDictionary(map);
        RedisUtil.setObj(roomUserStatusCache);
    }

    @Override
    public void setUserStatus(int userId, int subjectRoomId, int status) {
        SubjectRoomUserStatusCouchBaseObject obj = new SubjectRoomUserStatusCouchBaseObject();
        obj.setSubjectRoomId(subjectRoomId);
        obj.getUserStatusDictionary().put(userId, status);
        RedisUtil.updateCas(obj, CommonConst.MINUTE_1440);
    }

    @Override
    public HashMap<Integer, Integer> getUserStatus(int subjectRoomId) {
        SubjectRoomUserStatusCouchBaseObject cache = new SubjectRoomUserStatusCouchBaseObject(subjectRoomId);
        cache = RedisUtil.getObj(cache.getKey(), SubjectRoomUserStatusCouchBaseObject.class);
        if (cache == null) {
            return new HashMap<>();
        }
        return cache.getUserStatusDictionary();
    }

    @Override
    public HashMap<Integer, Boolean> getUserOnlineStatus(int subjectRoomId) {
        SubjectRoomUserOnlineCouchBaseObject cache = new SubjectRoomUserOnlineCouchBaseObject(subjectRoomId);
        cache = RedisUtil.getObj(cache.getKey(), SubjectRoomUserOnlineCouchBaseObject.class);
        if (cache == null) {
            return new HashMap<>();
        }
        return cache.getUserOnlineDictionary();
    }

    /// <summary>
    /// 从数据库中获取用户所在教室的信息
    /// </summary>
    /// <param name="userId"></param>
    /// <param name="userTypeId"></param>
    /// <param name="subjectId"></param>
    /// <returns></returns>
    private UserSubjectRoomCouchBaseObject getUserSubjectRoomInfoFromDb(int userId, int userTypeId, int subjectId) {
        UserSubjectRoomCouchBaseObject obj = new UserSubjectRoomCouchBaseObject(userId, subjectId);
        SubjectRoom room = null;
        if (userTypeId == UserTypeEnum.Student.getCode()) {
            room = subjectRoomService.getSubjectRoomByStudentId(subjectId, userId);
            if (room != null) {
                obj.setRoomUserType(0);
                obj.setSubjectRoomId(room.getSubjectRoomId());
                return obj;
            }
        } else if (userTypeId == UserTypeEnum.Teacher.getCode()) {
            room = subjectRoomService.getSubjectRoomList(subjectId).stream().filter(m -> m.getTeacherId() == userId).findFirst().orElse(null);
            if (room != null) {
                obj.setRoomUserType(1);
                obj.setSubjectRoomId(room.getSubjectRoomId());
                return obj;
            }
            room = subjectRoomService.getSubjectRoomByTeacherId(subjectId, userId);
            if (room != null) {
                obj.setRoomUserType(2);
                obj.setSubjectRoomId(room.getSubjectRoomId());
                return obj;
            }
            var subject = subjectService.getByKey(subjectId, true);
            if (subject.getTeacherId() == userId) {
                var rooms = subjectRoomService.getSubjectRoomBySubjectId(subjectId);
                if (CollectionUtils.isNotEmpty(rooms)) {
                    room = rooms.get(0);
                    obj.setRoomUserType(3);
                    obj.setSubjectRoomId(room.getSubjectRoomId());
                    return obj;
                }
            }
        }
        return null;
    }
}
