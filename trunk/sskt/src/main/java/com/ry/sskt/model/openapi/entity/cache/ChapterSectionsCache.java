package com.ry.sskt.model.openapi.entity.cache;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ry.sskt.core.annotation.IRedisStoredObject;
import com.ry.sskt.model.common.entity.ChapterSection;
import com.ry.sskt.model.openapi.entity.CourseMapping;
import io.protostuff.Tag;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class ChapterSectionsCache implements Serializable, IRedisStoredObject {

    private int courseMappingId;

    private List<ChapterSection> chapterSections;

    private static String key = "ChapterSections:CourseMappingId_%s";
    public ChapterSectionsCache()
    {}

    public ChapterSectionsCache(Integer courseMappingId, List<ChapterSection> chapterSections)
    {
        this.courseMappingId = courseMappingId;
        this.chapterSections = chapterSections;
    }

    public ChapterSectionsCache(Integer courseMappingId)
    {
        this.courseMappingId = courseMappingId;
    }
    @Override
    public String getKey() {
        return StringUtils.format(key, courseMappingId);
    }
}
