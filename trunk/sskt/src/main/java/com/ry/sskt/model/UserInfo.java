package com.ry.sskt.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.core.annotation.IRedisStoredObject;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 用户信息表
 */
@Data
@Accessors(chain = true)
public class UserInfo implements IRedisStoredObject {

    //主键ID 自动增长
    private Integer id;
    //真实姓名
    private String name;
    //性别 1：男 2：女
    private Integer sex;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @Override
    public String getKey() {
        return String.format("User%s", id);
    }
}
