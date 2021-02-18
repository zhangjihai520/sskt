package com.ry.sskt.model.common.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CourseMappingInfo {
    @JSONField(name = "CourseMappingId")
    private int courseMappingId;
    @JSONField(name = "CourseMappingName")
    private String courseMappingName;
    @JSONField(name = "BookVersionId")
    private int bookVersionId;

    public CourseMappingInfo() {
    }

    public CourseMappingInfo(int courseMappingId, String courseMappingName, int bookVersionId) {
        this.courseMappingId = courseMappingId;
        this.courseMappingName = courseMappingName;
        this.bookVersionId = bookVersionId;
    }
}
