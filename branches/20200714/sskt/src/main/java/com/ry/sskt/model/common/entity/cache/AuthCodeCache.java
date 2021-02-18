package com.ry.sskt.model.common.entity.cache;

import com.ry.sskt.core.annotation.IRedisStoredObject;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class AuthCodeCache implements Serializable, IRedisStoredObject {

    private int TeacherId;

    private int SubjectId;

    private String Code;

    private LocalDateTime PushTime;

    private static String key = "AuthCode_%s_%s";

    @Override
    public String getKey() {
        return String.format(key, TeacherId, SubjectId);
    }
}
