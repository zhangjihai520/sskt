package com.ry.sskt.model.dledc.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.common.request.RequestBase;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 教辅平台登录Request
 */
@Data
@Accessors(chain = true)
public class DledcLoginApiRequest extends RequestBase {
    /// <summary>
    /// 云平台通过回调地址传来的code
    /// </summary>
    @JSONField(name = "Code")
    private String code;
    @JSONField(name = "Name")
    private String name;
    @JSONField(name = "Uid")
    private String uid;
    @JSONField(name = "T")
    private String t;

    /// <summary>
    /// 1教辅双师课堂,2在线互动实验室
    /// </summary>
    @JSONField(name = "AccessTypeId")
    private int accessTypeId;

    /// <summary>
    /// 是否是测试，1为测试
    /// </summary>
    @JSONField(name = "IsTest")
    private int isTest;
}
