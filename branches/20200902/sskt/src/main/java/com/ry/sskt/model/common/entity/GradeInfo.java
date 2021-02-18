package com.ry.sskt.model.common.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 年级Info
 */
@Data
@Accessors(chain = true)
public class GradeInfo extends IdName {
    @JSONField(name = "CourseTypeId")
    private String courseTypeId;
    public GradeInfo() {
    }
    public GradeInfo(String courseTypeId, String id, String name) {
        super(id, name);
        this.courseTypeId = courseTypeId;
    }
}
