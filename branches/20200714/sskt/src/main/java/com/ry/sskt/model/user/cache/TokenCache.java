package com.ry.sskt.model.user.cache;

import com.ry.sskt.core.annotation.IRedisStoredObject;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class TokenCache implements Serializable, IRedisStoredObject {

    private int userId;

    private int fromTypeId;

    private String token;

    private static String key = "Token:%s:%s";

    @Override
    public String getKey() {
        return String.format(key, userId, fromTypeId);
    }
}
