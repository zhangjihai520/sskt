package com.ry.sskt.model.common.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 学段年级表
 * </p>
 *
 * @author xrq
 * @since 2020-04-17
 */
@Data
@Accessors(chain = true)
public class CourseTypeGrade {

    private static final long serialVersionUID = 1L;


    /**
     * 排序
     */
    @JSONField(name = "OrderIndex")
    private int orderIndex;
    /**
     * 默认学段年级(小学-7 初中-1 高中-4)
     */
    @JSONField(name = "StudyYearOffSet")
    private int studyYearOffSet;

    /**
     * 学科类型ID
     */
    @JSONField(name = "CourseTypeId")
    private int courseTypeId;

    /**
     * 年级id
     */
    @JSONField(name = "GradeId")
    private int gradeId;
    /**
     * 年级名称
     */
    @JSONField(name = "GradeName")
    private String gradeName;
    public CourseTypeGrade()
    {}
    public CourseTypeGrade(Integer orderIndex, int studyYearOffSet, int courseTypeId, int gradeId, String gradeName) {
        this.orderIndex = orderIndex;
        this.studyYearOffSet = studyYearOffSet;
        this.courseTypeId = courseTypeId;
        this.gradeId = gradeId;
        this.gradeName = gradeName;
    }
}
