package com.ry.sskt.model.openapi.request;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

/**
 * <描述类的作用>
 *
 * @author 幸仁强
 * @ClassName: RequestBase
 * @date 2018年12月17日
 */
@Data
@Accessors(chain = true)
public class OpenApiRequestBase {
    // 请求的AppKey
    private String appKey;

    // 随机字符串
    private String nonceStr;

    // 签名的Sign
    private String sign;
    public OpenApiRequestBase(){}
    /// <summary>
    /// 构造函数
    /// </summary>
    /// <param name="appKey"></param>
    public OpenApiRequestBase(String appKey)
    {
        this.appKey = appKey;
        this.nonceStr = UUID.randomUUID().toString();
    }
    public OpenApiRequestBase(String appKey, String nonceStr) {
        this.appKey = appKey;
        this.nonceStr = nonceStr;
    }
}
