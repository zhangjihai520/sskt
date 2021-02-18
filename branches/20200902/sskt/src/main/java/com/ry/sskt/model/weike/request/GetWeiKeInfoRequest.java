package com.ry.sskt.model.weike.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.common.request.RequestBase;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 读取微课请求
 */
@Data
@Accessors(chain = true)
public class GetWeiKeInfoRequest extends RequestBase {
    /// <summary>
    /// 微课的Id
    /// </summary>
    private String WeiKeId;
}
