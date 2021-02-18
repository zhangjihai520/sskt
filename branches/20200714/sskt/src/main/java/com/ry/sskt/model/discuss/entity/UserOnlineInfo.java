package com.ry.sskt.model.discuss.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserOnlineInfo {
    /// <summary>
    /// 用户Id
    /// </summary>
    @JSONField(name="UserId")
    public String userId;

    /// <summary>
    /// 用户名称
    /// </summary>
    @JSONField(name="UserName")
    public String userName;

    /// <summary>
    /// 用户头像
    /// </summary>
    @JSONField(name="UserFace")
    public String userFace;

    /// <summary>
    /// 是否在线
    /// </summary>
    @JSONField(name="IsOnline")
    public boolean isOnline;

    /// <summary>
    /// 1学生，2老师，3管理员，4家长
    /// </summary>
    @JSONField(name="UserTypeId")
    public int userTypeId;
}
