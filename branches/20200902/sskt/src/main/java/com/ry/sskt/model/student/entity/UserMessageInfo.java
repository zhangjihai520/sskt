package com.ry.sskt.model.student.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/// <summary>
/// 用户消息
/// </summary>
@Data
@Accessors(chain = true)
public class UserMessageInfo {
    /// <summary>
    /// 消息ID，加密
    /// </summary>
    private int MessageId;

    /// <summary>
    /// 消息源用户id，加密
    /// </summary>
    private int SourseUserId;

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
    /// 用户角色(1学生，2老师，3管理员，4家长),多个角色以英文逗号分隔
    /// </summary>
    private String UserRoles;

    /// <summary>
    /// 创建时间
    /// </summary>
    private LocalDateTime CreateDateTime;

    /// <summary>
    /// 用户类型
    /// </summary>
    private int UserTypeId;
}
