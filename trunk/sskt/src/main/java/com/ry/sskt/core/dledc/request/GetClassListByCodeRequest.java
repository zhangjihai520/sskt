package com.ry.sskt.core.dledc.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.core.dledc.IDledcGetRequest;
import com.ry.sskt.core.dledc.response.GetClassListByCodeResponse;
import com.ry.sskt.core.dledc.response.GetUserBaseInfoResponse;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/// <summary>
/// 根据组织code获取班级列表Request
/// </summary>
@Data
public class GetClassListByCodeRequest implements IDledcGetRequest<GetClassListByCodeResponse> {
    /// <summary>
    /// 用户类型，默认为空，可填值:teacher（老师）,student（学生）,parent(家长),org(机构人员)
    /// </summary>
    @JSONField(name = "OrganizeId")
    private String organizeId;
    public GetClassListByCodeRequest() {
    }

    public GetClassListByCodeRequest(String organizeId) {
        this.organizeId = organizeId;
    }

    @Override
    public Map<String, String> GetParameters() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("OrganizeCode", getOrganizeId());
        return map;
    }

    @Override
    public String GetApiPath() {
        return "api/api/department/GetClassListByCode";
    }
}
