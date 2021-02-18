package com.ry.sskt.model.common.entity.cache;

import com.ry.sskt.core.annotation.IRedisOperationObject;
import com.ry.sskt.core.annotation.IRedisStoredObject;
import com.ry.sskt.core.utils.ProtoBufUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class SyncUserLockStatusCache implements Serializable, IRedisStoredObject, IRedisOperationObject {


    //true已锁定  false未锁定
    private boolean isLock;

    private LocalDateTime lockDatetime;
    private static String key = "SyncUserLock";

    @Override
    public String getKey() {
        return key;
    }


    @Override
    public boolean operation(byte[] objByte) throws IOException {
        if (objByte != null) {
            SyncUserLockStatusCache obj = ProtoBufUtil.deserializer(objByte, SyncUserLockStatusCache.class);
            if (obj.isLock()) {
                Duration duration = Duration.between(obj.getLockDatetime(), LocalDateTime.now());
                long minutes = duration.toMinutes();
                if (minutes < 60) {
                    return false;
                }
            }
        }
        this.isLock = true;
        this.lockDatetime = LocalDateTime.now();
        return true;
    }
}
