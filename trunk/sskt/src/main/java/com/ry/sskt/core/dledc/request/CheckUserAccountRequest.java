package com.ry.sskt.core.dledc.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.core.dledc.IDledcGetRequest;
import com.ry.sskt.core.dledc.response.CheckUserAccountResponse;
import com.ry.sskt.core.dledc.response.GetUserBaseInfoResponse;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Data
public class CheckUserAccountRequest implements IDledcGetRequest<CheckUserAccountResponse> {
    /// <summary>
    /// 用户名
    /// </summary>
    @JSONField(name = "UserName")
    private String userName;

    /// <summary>
    /// 密码
    /// </summary>
    @JSONField(name = "UserPassword")
    private String userPassword;

    public CheckUserAccountRequest() {
    }

    public CheckUserAccountRequest(String userName, String userPassword) {
        this.userName = userName;
        this.userPassword = userPassword;
    }

    @Override
    public Map<String, String> GetParameters() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("UserName", getUserName());
        map.put("UserPassword", getUserPassword());
        return map;
    }

    @Override
    public String GetApiPath() {
        return "api/api/users/checkUserAccount";
    }
}
