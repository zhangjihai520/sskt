package com.ry.sskt.model.common.response;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.common.constant.Result;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Auther: 幸仁强
 * @Date: 2020/4/16 15:34
 * @Description: 返回实体对象基类
 */
@Data
@Accessors(chain = true)
public class RtmpResponse {
    @JSONField(name = "code")
    private String code;
    @JSONField(name = "detail")
    private String detail;

    public RtmpResponse(String code, String detail) {

    }

    public RtmpResponse() {

    }

    /**
     * 验证失败 公共异常
     *
     * @return
     */
    public static String GetSuccessResponse() {
        return JSON.toJSONString(new RtmpResponse("200", "SUCCESS"));
    }

    /**
     * 验证失败 公共异常
     *
     * @return
     */
    public static String GetValidateErrorResponse() {
        return JSON.toJSONString(new RtmpResponse("401", "VALIDATED FAIL"));
    }

    /**
     * 验证失败 公共异常
     *
     * @return
     */
    public static String GetErrorResponse() {
        return JSON.toJSONString(new RtmpResponse("500", "ERROR"));
    }
}
