package com.ry.sskt.model.common.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.core.annotation.IRedisStoredObject;
import io.protostuff.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 学科表
 * </p>
 *
 * @author xrq
 * @since 2020-04-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User implements IRedisStoredObject {
    private static String key = "User:UserId_%s";

    @Override
    public String getKey() {
        return String.format(key, this.userId);
    }

    /// <summary>
    /// 用户id
    /// </summary>

    @JSONField(name = "UserId")
    private int userId;

    /// <summary>
    /// 用户名
    /// </summary>

    @JSONField(name = "UserName")
    private String userName;

    /// <summary>
    /// 密码
    /// </summary>

    @JSONField(name = "Password")
    private String password;

    /// <summary>
    /// 用户类型，1学生，2老师，3管理员，4家长
    /// </summary>

    @JSONField(name = "UserTypeId")
    private int userTypeId;

    /// <summary>
    /// 用户真名
    /// </summary>

    @JSONField(name = "UserTrueName")
    private String userTrueName;

    /// <summary>
    /// 性别，1男，2女，3其他
    /// </summary>

    @JSONField(name = "UserSex")
    private int userSex;

    /// <summary>
    /// 用户头像
    /// </summary>

    @JSONField(name = "UserFace")
    private String userFace;

    /// <summary>
    /// 邮箱
    /// </summary>

    @JSONField(name = "EmailAddress")
    private String emailAddress;

    /// <summary>
    /// 学校名
    /// </summary>

    @JSONField(name = "SchoolName")
    private String schoolName;

    /// <summary>
    /// 个人简介
    /// </summary>

    @JSONField(name = "Comment")
    private String comment;

    /// <summary>
    /// 手机号码
    /// </summary>

    @JSONField(name = "Phone")
    private String phone;

    /// <summary>
    /// 年级Id
    /// </summary>

    @JSONField(name = "GradeId")
    private int gradeId;

    /// <summary>
    /// 用户来源类型，1教辅平台，2导入学生，3注册
    /// </summary>

    @JSONField(name = "SourceTypeId")
    private int sourceTypeId;

    /// <summary>
    /// 用户来源id
    /// </summary>

    @JSONField(name = "SourceId")
    private String sourceId;

    /// <summary>
    /// 状态，0删除，1未删除
    /// </summary>

    @JSONField(name = "StatusFlag")
    private int statusFlag;

    /// <summary>
    /// 创建时间
    /// </summary>

    @JSONField(name = "CreateDateTime")
    private LocalDateTime createDateTime;

    /// <summary>
    /// 更新时间
    /// </summary>

    @JSONField(name = "UpdateDateTime")
    private LocalDateTime updateDateTime;

    /// <summary>
    /// 用户角色(1学生，2老师，3管理员，4家长),多个角色以英文逗号分隔
    /// </summary>

    @JSONField(name = "UserRoles")
    private String userRoles;

    /// <summary>
    /// 学籍号
    /// </summary>

    @JSONField(name = "StudentCode")
    private String studentCode;

    /// <summary>
    /// 身份证号
    /// </summary>
    @JSONField(name = "IdCard")
    private String idCard;

    @JSONField(name = "RoomUserType")
    private int roomUserType;

    @JSONField(name = "OrderIndex")
    private int orderIndex;
}
