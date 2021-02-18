package com.ry.sskt.model.openapi.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GetCourseMappingRequest extends OpenApiRequestBase {
    private Integer bookVersionId;

    public GetCourseMappingRequest(String appKey, String nonceStr, int bookVersionId) {
        super(appKey, nonceStr);
        this.bookVersionId = bookVersionId;
    }

    public GetCourseMappingRequest() {

    }
}
