package com.ry.sskt.model.common.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.common.entity.CourseMappingInfo;
import com.ry.sskt.model.common.entity.SimpleTreeNode;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class GetChapterSectionListResponse {
    @JSONField(name = "ChapterSectionList")
    private List<SimpleTreeNode> chapterSectionList;

    public GetChapterSectionListResponse() {
    }

    public GetChapterSectionListResponse(List<SimpleTreeNode> chapterSectionList) {
        this.chapterSectionList = chapterSectionList;
    }
}
