package com.ry.sskt.model.common.entity.cache;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.core.annotation.IRedisStoredObject;
import com.ry.sskt.model.common.entity.view.UserLoginTotalView;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@Accessors(chain = true)
public class UserLoginTotalCouchBaseObject implements Serializable, IRedisStoredObject {

    /// <summary>
    /// 登录日期
    /// </summary>
    @JSONField(name = "LoginDate")
    public LocalDate loginDate;

    /// <summary>
    /// 统计数据
    /// </summary>
    @JSONField(name = "ObjectList")
    public List<UserLoginTotalView> objectList;

    public UserLoginTotalCouchBaseObject() {
    }

    public UserLoginTotalCouchBaseObject(LocalDate loginDate, List<UserLoginTotalView> objectList) {
        this.loginDate = loginDate;
        this.objectList = objectList;
    }

    private static String key = "UserLoginTotal:LoginDate_%s";

    @Override
    public String getKey() {
        DateTimeFormatter dtf3 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return String.format(key, loginDate.format(dtf3));
    }

}
