package com.ry.sskt.model.discuss.entity.cache;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.core.annotation.IRedisStoredObject;
import com.ry.sskt.model.common.entity.User;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

@Data
@Accessors(chain = true)
public class SubjectRoomUserListCouchBaseObject implements IRedisStoredObject {

    /// <summary>
    /// 教室Id
    /// </summary>

    @JSONField(name = "SubjectRoomId")
    private int subjectRoomId;
    /// <summary>
    /// 信息列表
    /// </summary>

    @JSONField(name = "UserList")
    private List<User> userList;

    private static String key = "SubjectRoomUsers:SubjectRoomId_%s";


    @Override
    public String getKey() {
        return String.format(key, this.subjectRoomId);
    }

    public SubjectRoomUserListCouchBaseObject() {
        this.userList = new LinkedList<User>();
    }

    public SubjectRoomUserListCouchBaseObject(Integer subjectRoomId) {
        this.subjectRoomId = subjectRoomId;
    }
}
