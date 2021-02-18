package com.ry.sskt.model.openapi.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 教材版本
 */
@Data
@Accessors(chain = true)
public class BookVersion {
    @JSONField(name="BookVersionId")
    private int bookVersionId;
    @JSONField(name="BookVersionName")
    private String bookVersionName;
    @JSONField(name="CourseId")
    private int courseId;
}
