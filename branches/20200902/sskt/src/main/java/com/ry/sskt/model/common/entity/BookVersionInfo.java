package com.ry.sskt.model.common.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BookVersionInfo {
    @JSONField(name = "BookVersionId")
    private int bookVersionId;
    @JSONField(name = "BookVersionName")
    private String bookVersionName;
    @JSONField(name = "CourseId")
    private int courseId;

    public BookVersionInfo() {
    }

    public BookVersionInfo(int bookVersionId, String bookVersionName, int courseId) {
        this.bookVersionId = bookVersionId;
        this.bookVersionName = bookVersionName;
        this.courseId = courseId;
    }
}
