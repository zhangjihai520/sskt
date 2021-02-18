package com.ry.sskt.service;

import com.ry.sskt.core.dledc.DledcDefaultClient;
import com.ry.sskt.model.common.entity.TwoTuple;

/**
 * 云平台业务层接口
 */
public interface IDledcService {
    /// <summary>
    /// 同步教辅平台用户
    /// </summary>
    /// <returns></returns>
    TwoTuple<Integer, Integer> sychroUser(String uid) throws Exception;

    /// <summary>
    /// 获取教辅平台对接参数
    /// </summary>
    /// <param name="accessTypeId">1教辅双师课堂,2在线互动实验室</param>
    /// <returns></returns>
    DledcDefaultClient getDledcConfig(int accessTypeId);
}
