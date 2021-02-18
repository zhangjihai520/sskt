package com.ry.sskt.model.common.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.common.entity.BookVersionInfo;
import com.ry.sskt.model.common.entity.CourseInfo;
import com.ry.sskt.model.common.entity.GradeInfo;
import com.ry.sskt.model.common.entity.IdName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

/**
 * 返回年级/学科等信息
 */
@Data
@Accessors(chain = true)
public class GetGradeListResponse {
    @JSONField(name = "CourseTypeList")
    private List<IdName> courseTypeList;
    @JSONField(name = "GradeList")
    private List<GradeInfo> gradeList;
    @JSONField(name = "CourseList")
    private List<CourseInfo> courseList;

    public GetGradeListResponse() {
        this.courseTypeList = new LinkedList<IdName>();
        this.gradeList = new LinkedList<GradeInfo>();
        this.courseList = new LinkedList<CourseInfo>();
    }

    public GetGradeListResponse(List<IdName> courseTypeList, List<GradeInfo> gradeList, List<CourseInfo> courseList) {
        this.courseTypeList = courseTypeList;
        this.gradeList = gradeList;
        this.courseList = courseList;
    }
}
