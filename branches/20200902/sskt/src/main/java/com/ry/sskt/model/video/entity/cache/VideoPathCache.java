package com.ry.sskt.model.video.entity.cache;

import com.ry.sskt.core.annotation.IRedisStoredObject;
import com.ry.sskt.model.common.entity.Grade;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

@Data
@Accessors(chain = true)
public class VideoPathCache implements Serializable, IRedisStoredObject {


    private int SubjectId;

    private int TypeId;

    private String Path;

    private static String key = "SubjectVideoPath:SubjectId_%s:TypeId_%s";

    @Override
    public String getKey() {
        return String.format(key, SubjectId, TypeId);
    }
}
