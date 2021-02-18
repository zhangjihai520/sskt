package com.ry.sskt.model.common.entity.cache;

import com.ry.sskt.core.annotation.IRedisStoredObject;
import com.ry.sskt.model.common.entity.Course;
import com.ry.sskt.model.common.entity.CourseType;
import io.protostuff.Tag;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class CourseCache implements Serializable, IRedisStoredObject {


    private List<Course> courses;

    private static String key = "Courses";
    @Override
    public String getKey() {
        return key;
    }
}
