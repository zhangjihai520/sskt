package com.ry.sskt.service;

import com.ry.sskt.core.dledc.DledcDefaultClient;
import com.ry.sskt.model.common.entity.UserLoginInfo;
import com.ry.sskt.model.common.entity.view.UserLoginTotalView;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * 云平台业务层接口
 */
public interface IUserLoginInfoService {
    /// <summary>
    /// 表Userlogininfo的IBLL
    /// 此实体由生成器自动生成
    /// </summary>
    void save(UserLoginInfo dataModel) throws Exception;

    /// <summary>
    /// 获取某天登录学段用户数
    /// </summary>
    /// <param name="dateTime"></param>
    List<UserLoginTotalView> getGradeTotal(LocalDate dateTime);
}
