package com.ry.sskt.core.dledc.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// <summary>
/// 用户信息
/// </summary>
@Data
@Accessors(chain = true)
public class UserBaseInfo {
    /// <summary>
    /// 用户名
    /// </summary>
    @JSONField(name = "Account")
    private String account;

    /// <summary>
    /// 密码
    /// </summary>
    @JSONField(name = "Password")
    private String password;

    /// <summary>
    /// 用户真名
    /// </summary>
    @JSONField(name = "RealName")
    private String realName;

    /// <summary>
    /// 性别
    /// </summary>
    @JSONField(name = "Gender")
    private Boolean gender;

    /// <summary>
    /// 用户头像
    /// </summary>
    @JSONField(name = "HeadIcon")
    private String headIcon;

    /// <summary>
    /// 机构id
    /// </summary>
    @JSONField(name = "OrganizeId")
    private String organizeId;

    /// <summary>
    /// 部门id
    /// </summary>
    @JSONField(name = "DepartmentId")
    private String departmentId;

    /// <summary>
    /// email
    /// </summary>
    @JSONField(name = "Email")
    private String email;

    /// <summary>
    /// 学校名称
    /// </summary>
    @JSONField(name = "OrganizeName")
    private String organizeName;

    /// <summary>
    /// 备注
    /// </summary>
    @JSONField(name = "Description")
    private String description;

    /// <summary>
    /// 手机号码
    /// </summary>
    @JSONField(name = "MobilePhone")
    private String mobilePhone;

    /// <summary>
    /// 年级
    /// </summary>
    @JSONField(name = "GradeCode")
    private String gradeCode;

    /// <summary>
    /// 用户id
    /// </summary>
    @JSONField(name = "Uid")
    private String uid;

    /// <summary>
    /// 学籍号
    /// </summary>
    @JSONField(name = "StudentCode")
    private String studentCode;

    /// <summary>
    /// 用户类型
    /// </summary>
    @JSONField(name = "UserType")
    private String userType;

    /// <summary>
    /// 用户角色
    /// </summary>
    @JSONField(name = "UserRoles")
    private List<String> userRoles;

    /// <summary>
    /// 身份证号码
    /// </summary>
    @JSONField(name = "IdCard")
    private String idCard;
}
