package com.ry.sskt.model.openapi.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GetBookVersionOpenApiRequest extends OpenApiRequestBase {
    private int courseId;

    public GetBookVersionOpenApiRequest(String appKey, String nonceStr, int courseId) {
        super(appKey,nonceStr);
        this.courseId = courseId;
    }

    public GetBookVersionOpenApiRequest() {

    }
}
