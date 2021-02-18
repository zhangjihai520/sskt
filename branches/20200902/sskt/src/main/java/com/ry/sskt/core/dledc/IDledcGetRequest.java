package com.ry.sskt.core.dledc;

import java.util.Map;

/// <summary>
/// get请求
/// </summary>
/// <typeparam name="T"></typeparam>
public interface IDledcGetRequest<T extends DledcResponse> extends IDledcRequest<T> {
    /// <summary>
    ///获取所有的Key-Value形式的文本请求参数字典。其中：
    /// Key: 请求参数名
    /// Value: 请求参数文本值
    /// </summary>
    /// <returns>文本请求参数字典</returns>
    Map<String, String> GetParameters();
}
