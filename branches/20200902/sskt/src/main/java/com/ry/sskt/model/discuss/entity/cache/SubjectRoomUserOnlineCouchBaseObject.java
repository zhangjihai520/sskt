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
public class SubjectRoomUserOnlineCouchBaseObject implements IRedisStoredObject, IRedisOperationObject {

    /// <summary>
    /// 教室Id
    /// </summary>
    @JSONField(name = "SubjectRoomId")
    private int subjectRoomId;
    @JSONField(name = "UserOnlineDictionary")
    private HashMap<Integer, Boolean> userOnlineDictionary;

    private static String key = "SubjectRoomUserOnline:SubjectRoomId_%s";

    @Override
    public String getKey() {
        return String.format(key, this.subjectRoomId);
    }

    public SubjectRoomUserOnlineCouchBaseObject() {
        this.userOnlineDictionary = new HashMap<Integer, Boolean>();
    }

    public SubjectRoomUserOnlineCouchBaseObject(Integer subjectRoomId) {
        this.subjectRoomId = subjectRoomId;
    }

    @Override
    public boolean operation(byte[] objByte) throws IOException {
        if (objByte != null) {
            SubjectRoomUserOnlineCouchBaseObject obj = ProtoBufUtil.deserializer(objByte, SubjectRoomUserOnlineCouchBaseObject.class);
            obj.getUserOnlineDictionary().putAll(userOnlineDictionary);
            setUserOnlineDictionary(obj.getUserOnlineDictionary());
        }
        return true;
    }
}
