package com.ry.sskt.model.discuss.entity.cache;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.core.annotation.IRedisStoredObject;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserSubjectRoomCouchBaseObject implements IRedisStoredObject {
    /// <summary>
    /// 课程Id
    /// </summary>

    @JSONField(name = "UserId")
    private int userId;

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

    private static String key = "TalkMessageUserSubjectRoomRelate:UserId_%s_SubjectRoomId_%s";

    @Override
    public String getKey() {
        return String.format(key, this.userId, this.subjectRoomId);
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
