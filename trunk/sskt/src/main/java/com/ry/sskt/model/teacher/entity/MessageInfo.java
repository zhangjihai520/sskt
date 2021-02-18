package com.ry.sskt.model.teacher.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/// <summary>
/// 消息
/// </summary>
@Data
@Accessors(chain = true)
public class MessageInfo {
    /// <summary>
    /// 消息ID，加密
    /// </summary>
    private String MessageId;

    /// <summary>
    /// 用户id，加密
    /// </summary>
    private String UserId;

    /// <summary>
    /// 用户名称
    /// </summary>
    private String UserTrueName;

    /// <summary>
    /// 用户头像
    /// </summary>
    private String UserFace;

    /// <summary>
    /// 消息内容
    /// </summary>
    private String Content;

    /// <summary>
    /// 回复状态  1:已回复，0:未回复
    /// </summary>
    private int IsReply;

    /// <summary>
    /// 消息状态，0未读、1已读
    /// </summary>
    private int StatusFlag;

    /// <summary>
    /// 用户类型，1学生，2老师，3机构人员，4家长
    /// </summary>
    private int UserTypeId;

    /// <summary>
    /// 创建时间
    /// </summary>
    private String CreateDateTime;
}
