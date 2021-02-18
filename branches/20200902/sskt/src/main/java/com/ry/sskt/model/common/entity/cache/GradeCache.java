package com.ry.sskt.model.common.entity.cache;

import com.ry.sskt.core.annotation.IRedisStoredObject;
import com.ry.sskt.model.common.entity.Course;
import com.ry.sskt.model.common.entity.Grade;
import io.protostuff.Tag;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class GradeCache implements Serializable, IRedisStoredObject {


    private List<Grade> grades;

    private static String key = "Grades";
    @Override
    public String getKey() {
        return key;
    }
}
