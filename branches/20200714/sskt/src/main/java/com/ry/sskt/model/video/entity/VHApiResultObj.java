package com.ry.sskt.model.video.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 硬件返回的基本对象
 */
@Data
@Accessors(chain = true)
public class VHApiResultObj{
    /// <summary>
    /// 1:成功，0：失败
    /// </summary>
    private int nResult;

    /// <summary>
    /// 结果Message
    /// </summary>
    private String msg;
}
