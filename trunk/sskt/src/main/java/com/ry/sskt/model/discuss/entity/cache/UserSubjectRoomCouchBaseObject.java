package com.ry.sskt.model.discuss.entity.cache;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.core.annotation.IRedisStoredObject;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserSubjectRoomCouchBaseObject implements IRedisStoredObject {
    /// <summary>
    /// 用户id
    /// </summary>
    @JSONField(name = "UserId")
    private int userId;

    /**
     *房间用户类型  0学生 1助教  2旁听 3主讲
     */
    @JSONField(name = "RoomUserType")
    private int roomUserType;

    /// <summary>
    /// 教室Id
    /// </summary>
    @JSONField(name = "SubjectId")
    private int subjectId;

    /// <summary>
    /// 信息列表
    /// </summary>
    @JSONField(name = "SubjectRoomId")
    private int subjectRoomId;
    private static String key = "UserSubjectRoom:SubjectRoomId_%s:UserId_%s";

    @Override
    public String getKey() {
        return String.format(key, this.subjectRoomId, this.userId);
    }

    public UserSubjectRoomCouchBaseObject(int userId, int subjectId) {
        this.userId = userId;
        this.subjectId = subjectId;
    }

    public UserSubjectRoomCouchBaseObject(int userId, int subjectId,int subjectRoomId) {
        this.userId = userId;
        this.subjectId = subjectId;
        this.subjectRoomId = subjectRoomId;
    }

}
