package com.ry.sskt.model.discuss.entity.cache;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.core.annotation.IRedisOperationObject;
import com.ry.sskt.core.annotation.IRedisStoredObject;
import com.ry.sskt.core.utils.ProtoBufUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
public class SubjectRoomUserStatusCouchBaseObject implements IRedisStoredObject, IRedisOperationObject {

    /// <summary>
    /// 教室Id
    /// </summary>
    @JSONField(name = "SubjectRoomId")
    private int subjectRoomId;
    //用户状态 0，正常  1禁言  3黑名单
    @JSONField(name = "UserStatusDictionary")
    private HashMap<Integer, Integer> userStatusDictionary;

    private static String key = "SubjectRoomUserStatus:SubjectRoomId_%s";

    @Override
    public String getKey() {
        return String.format(key, this.subjectRoomId);
    }

    public SubjectRoomUserStatusCouchBaseObject() {
        this.userStatusDictionary = new HashMap();
    }

    public SubjectRoomUserStatusCouchBaseObject(Integer subjectRoomId) {
        this.subjectRoomId = subjectRoomId;
    }

    @Override
    public boolean operation(byte[] objByte) throws IOException {
        if (objByte != null) {
            SubjectRoomUserStatusCouchBaseObject obj = ProtoBufUtil.deserializer(objByte, SubjectRoomUserStatusCouchBaseObject.class);
            obj.getUserStatusDictionary().putAll(userStatusDictionary);
            setUserStatusDictionary(obj.getUserStatusDictionary());
        }
        return true;
    }
}
