package com.ry.sskt.core.dledc.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class CheckUserAccountUserInfo {
    /// <summary>
    /// 用户名
    /// </summary>
    private String Account;

    /// <summary>
    /// 密码
    /// </summary>
    private String Password;

    /// <summary>
    /// 用户真名
    /// </summary>
    private String RealName;

    /// <summary>
    /// 性别
    /// </summary>
    private Boolean Gender;

    /// <summary>
    /// 用户头像
    /// </summary>
    private String HeadIcon;

    /// <summary>
    /// 机构id
    /// </summary>
    private String OrganizeId;

    /// <summary>
    /// 部门id
    /// </summary>
    private String DepartmentId;

    /// <summary>
    /// email
    /// </summary>
    private String Email;

    /// <summary>
    /// 学校名称
    /// </summary>
    private String OrganizeName;

    /// <summary>
    /// 备注
    /// </summary>
    private String Description;

    /// <summary>
    /// 手机号码
    /// </summary>
    private String MobilePhone;

    /// <summary>
    /// 年级
    /// </summary>
    private String GradeCode;

    /// <summary>
    /// 用户id
    /// </summary>
    private String Uid;

    /// <summary>
    /// 学籍号
    /// </summary>
    private String StudentCode;

    /// <summary>
    /// 用户类型
    /// </summary>
    private String UserType;

    /// <summary>
    /// 用户角色
    /// </summary>
    private List<String> UserRoles;

    /// <summary>
    /// 身份证号码
    /// </summary>
    private String IdCard;
}
