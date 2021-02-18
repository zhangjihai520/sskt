package com.ry.sskt.model.openapi.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GetChapterSectionRequest extends OpenApiRequestBase {
    private Integer courseMappingId;

    public GetChapterSectionRequest(String appKey, String nonceStr, int courseMappingId) {
        super(appKey, nonceStr);
        this.courseMappingId = courseMappingId;
    }

    public GetChapterSectionRequest() {

    }
}
