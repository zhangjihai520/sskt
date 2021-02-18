package com.ry.sskt.model.common.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 学科表
 * </p>
 *
 * @author xrq
 * @since 2020-04-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Course {

    /**
     * 学科ID
     */
    @JSONField(name = "CourseId")

    private Integer courseId;

    /**
     * 学科名称
     */
    @JSONField(name = "CourseName")

    private String courseName;

    /**
     * 学科类别
     */
    @JSONField(name = "CourseTypeId")

    private Integer courseTypeId;

    /**
     * 学科简称
     */
    @JSONField(name = "CourseCode")

    private String courseCode;

    /**
     * 用来在UI展示的时候进行排序使用
     */
    @JSONField(name = "OrderIndex")

    private Integer orderIndex;

    /**
     * 用来记录学科的缩写，对查找对应的图片或者其它跟学科相关的资源有用
     */
    @JSONField(name = "ShortCode")

    private String shortCode;

    /**
     * 学科状态
     */
    @JSONField(name = "StatusFlag")

    private Integer statusFlag;

}
