package com.ry.sskt.model.user.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.core.dledc.DledcResponse;
import com.ry.sskt.core.dledc.response.GetUserBaseInfoResponseData;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 【用户信息】获取用户基本信息 返回参数
 */
@Data
@Accessors(chain = true)
public class GetUserBaseInfoResponse {
    /// <summary>
    /// 用户头像
    /// </summary>
    private String UserFace;

    /// <summary>
    /// 用户名
    /// </summary>
    private String UserName;

    /// <summary>
    /// 用户id，加密值
    /// </summary>
    private String UserId;

    /// <summary>
    /// 用户姓名
    /// </summary>
    private String UserTrueName;

    /// <summary>
    /// 用户角色(1学生，2老师，3管理员，4家长),多个角色以英文逗号分隔
    /// </summary>
    private String UserRoles;

    /// <summary>
    /// 用户年级，有的可能没有年级
    /// </summary>
    private String GradeId;
}
