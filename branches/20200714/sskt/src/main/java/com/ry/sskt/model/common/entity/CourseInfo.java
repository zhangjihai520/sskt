package com.ry.sskt.model.common.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class CourseInfo extends IdName {
    @JSONField(name = "LongName")
    private String longName;
    @JSONField(name = "CourseTypeId")
    private String courseTypeId;

    public CourseInfo() {
    }

    public CourseInfo(String courseTypeId, String id, String longName, String name) {
        super(id, name);
        this.longName = longName;
        this.courseTypeId = courseTypeId;
    }
}
