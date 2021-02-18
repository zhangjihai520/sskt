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
    public int userId;

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
    /// 0正常，1、禁言，2、黑名单
    /// </summary>
    @JSONField(name="Status")
    public int status;
    /// <summary>
    /// 1学生，2老师，3管理员，4家长
    /// </summary>
    @JSONField(name="UserTypeId")
    public int userTypeId;
    /**
     *房间用户类型  0学生 1助教  2旁听 3主讲
     */
    @JSONField(name="RoomUserType")
    public int roomUserType;

    @JSONField(name="OrderIndex")
    private int orderIndex;
}
