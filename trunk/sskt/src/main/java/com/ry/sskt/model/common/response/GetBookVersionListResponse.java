package com.ry.sskt.model.common.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.common.entity.BookVersionInfo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 获取教材版本
 */
@Data
@Accessors(chain = true)
public class GetBookVersionListResponse {
    @JSONField(name = "BookVersionList")
    private List<BookVersionInfo> bookVersionList;

    public GetBookVersionListResponse() {
    }

    public GetBookVersionListResponse(List<BookVersionInfo> bookVersionList) {
        this.bookVersionList = bookVersionList;
    }
}
