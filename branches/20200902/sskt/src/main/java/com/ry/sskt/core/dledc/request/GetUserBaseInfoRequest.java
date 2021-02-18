package com.ry.sskt.core.dledc.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.core.dledc.IDledcGetRequest;
import com.ry.sskt.core.dledc.response.GetUserBaseInfoResponse;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Data
public class GetUserBaseInfoRequest implements IDledcGetRequest<GetUserBaseInfoResponse> {
    /// <summary>
    /// 用户统一身份 uid
    /// </summary>
    @JSONField(name = "UId")
    private String uId;

    /// <summary>
    /// 用户类型，默认为空，可填值:teacher（老师）,student（学生）,parent(家长),org(机构人员)
    /// </summary>
    @JSONField(name = "UserType")
    private String userType;

    public GetUserBaseInfoRequest() {
    }

    public GetUserBaseInfoRequest(String uId) {
        this.uId = uId;
        this.userType = StringUtils.EMPTY;
    }

    public GetUserBaseInfoRequest(String uId, String userType) {
        this.uId = uId;
        this.userType = userType;
    }

    @Override
    public Map<String, String> GetParameters() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("uid", getUId());
        map.put("UserType", getUserType());
        return map;
    }

    @Override
    public String GetApiPath() {
        return "api/api/users/getbaseinfo";
    }
}
