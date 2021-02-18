package com.ry.sskt.model.openapi.entity.cache;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ry.sskt.core.annotation.IRedisStoredObject;
import com.ry.sskt.model.common.entity.Course;
import com.ry.sskt.model.openapi.entity.CourseMapping;
import io.protostuff.Tag;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class CourseMappingsCache implements Serializable, IRedisStoredObject {

    private int bookVerionId;

    private List<CourseMapping> courseMappings;

    private static String key = "CourseMappings_%s";

    public CourseMappingsCache() {
    }

    public CourseMappingsCache(Integer bookVerionId) {
        this.bookVerionId = bookVerionId;
    }

    public CourseMappingsCache(Integer bookVerionId, List<CourseMapping> courseMappings) {
        this.bookVerionId = bookVerionId;
        this.courseMappings = courseMappings;
    }

    @Override
    public String getKey() {
        return StringUtils.format(key, bookVerionId);
    }
}
