package com.ry.sskt.model.common.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 学段学科表
 * </p>
 *
 * @author xrq
 * @since 2020-04-17
 */
@Data
@Accessors(chain = true)
public class CourseTypeCourse {

    private static final long serialVersionUID = 1L;


    /**
     * 排序
     */
    @JSONField(name = "OrderIndex")
    private int orderIndex;
    /**
     * 默认学段年级(小学-7 初中-1 高中-4)
     */
    @JSONField(name = "SubIndex")
    private int subIndex;

    /**
     * 学科类型ID
     */
    @JSONField(name = "CourseTypeId")
    private int courseTypeId;

    /**
     * 年级id
     */
    @JSONField(name = "CourseId")
    private int courseId;
    /**
     * 年级名称
     */
    @JSONField(name = "CourseName")
    private String courseName;
    /**
     * 年级名称
     */
    @JSONField(name = "ShortCode")
    private String shortCode;
    public CourseTypeCourse(){}

    public CourseTypeCourse(Integer orderIndex, int subIndex, int courseTypeId, int courseId, String courseName, String shortCode) {
        this.orderIndex = orderIndex;
        this.subIndex = subIndex;
        this.courseTypeId = courseTypeId;
        this.courseId = courseId;
        this.courseName = courseName;
        this.shortCode = shortCode;
    }
}
