package com.ry.sskt.model.discuss.entity.cache;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.core.annotation.IRedisOperationObject;
import com.ry.sskt.core.annotation.IRedisStoredObject;
import com.ry.sskt.core.utils.ProtoBufUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.util.HashMap;

@Data
@Accessors(chain = true)
public class SubjectRoomUserOnlineCouchBaseObject implements IRedisStoredObject, IRedisOperationObject {

    /// <summary>
    /// 教室Id
    /// </summary>

    @JSONField(name = "SubjectRoomId")
    private int subjectRoomId;
    /// <summary>
    /// 信息列表
    /// </summary>

    @JSONField(name = "UserDictionary")
    private HashMap<Integer, Boolean> userDictionary;


    private static String key = "NTalkMessage:Online_%s";

    @Override
    public String getKey() {
        return String.format(key, this.subjectRoomId);
    }

    public SubjectRoomUserOnlineCouchBaseObject() {
        this.userDictionary = new HashMap<Integer, Boolean>();
    }

    public SubjectRoomUserOnlineCouchBaseObject(Integer subjectRoomId) {
        this.subjectRoomId = subjectRoomId;
    }

    @Override
    public boolean operation(byte[] objByte) throws IOException {
        if (objByte != null) {
            SubjectRoomUserOnlineCouchBaseObject obj = ProtoBufUtil.deserializer(objByte, SubjectRoomUserOnlineCouchBaseObject.class);
            obj.getUserDictionary().putAll(userDictionary);
            setUserDictionary(obj.getUserDictionary());
        }
        return true;
    }
}
