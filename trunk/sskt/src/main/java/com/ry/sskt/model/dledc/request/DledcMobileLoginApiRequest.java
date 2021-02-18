package com.ry.sskt.model.dledc.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.common.request.RequestBase;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 教辅平台移动端登录Request
 */
@Data
@Accessors(chain = true)
public class DledcMobileLoginApiRequest extends RequestBase {

    @JSONField(name = "Uid")
    private String uid;
    /// <summary>
    /// 是否是测试，1为测试
    /// </summary>
    @JSONField(name = "IsTest")
    private int isTest;
}
