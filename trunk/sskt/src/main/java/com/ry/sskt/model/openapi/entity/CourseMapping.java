package com.ry.sskt.model.openapi.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 教材版本
 */
@Data
@Accessors(chain = true)
public class CourseMapping {

    private int courseMappingId;

    private String courseMappingName;

    private int bookVersionId;
}
