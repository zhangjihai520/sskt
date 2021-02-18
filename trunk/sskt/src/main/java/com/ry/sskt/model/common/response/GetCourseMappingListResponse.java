package com.ry.sskt.model.common.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.common.entity.CourseMappingInfo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class GetCourseMappingListResponse {
    @JSONField(name = "CourseMappingList")
    private List<CourseMappingInfo> courseMappingList;

    public GetCourseMappingListResponse() {
    }

    public GetCourseMappingListResponse(List<CourseMappingInfo> courseMappingList) {
        this.courseMappingList = courseMappingList;
    }
}
