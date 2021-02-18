package com.ry.sskt.core.dledc;

import com.ry.sskt.core.dledc.DledcResponse;

public interface IDledcRequest<T extends DledcResponse> {
    /// <summary>
    /// 请求api路径
    /// </summary>
    /// <returns>api路径</returns>
    String GetApiPath();
}
