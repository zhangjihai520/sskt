package com.ry.sskt.model.common.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Accessors(chain = true)
public class UserLoginInfo {
    /// <summary>
    /// 用户Id
    /// </summary>
    private int UserId;

    /// <summary>
    /// 登录时间
    /// </summary>
    private LocalDateTime LoginTime;

    /// <summary>
    /// 登录日期
    /// </summary>
    private LocalDate LoginDate;

    /// <summary>
    /// 登录方式
    /// </summary>
    private int LoginType;
}
