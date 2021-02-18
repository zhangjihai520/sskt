package com.ry.sskt.model.openapi.entity.cache;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ry.sskt.core.annotation.IRedisStoredObject;
import com.ry.sskt.model.openapi.entity.BookVersion;
import com.ry.sskt.model.openapi.entity.CourseMapping;
import io.protostuff.Tag;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class BookVersionsCache implements Serializable, IRedisStoredObject {

    private int courseId;

    private List<BookVersion> bookVersions;

    private static String key = "BookVersions:CourseId_%s";

    public BookVersionsCache(){}

    public BookVersionsCache(Integer courseId)
    {
        this.courseId = courseId;
    }

    public BookVersionsCache(Integer courseId,List<BookVersion> bookVersions)
    {
        this.courseId = courseId;
        this.bookVersions = bookVersions;
    }

    @Override
    public String getKey() {
        return StringUtils.format(key, courseId);
    }
}
