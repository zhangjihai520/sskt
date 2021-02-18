package com.ry.sskt.model.common.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.core.annotation.IRedisStoredObject;
import io.protostuff.Tag;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 学段表
 * </p>
 *
 * @author xrq
 * @since 2020-04-17
 */
@Data
@Accessors(chain = true)
public class CourseType {

    private static final long serialVersionUID = 1L;

    /**
     * 学科类型ID
     */
    @JSONField(name = "CourseTypeId")
    private Integer courseTypeId;

    /**
     * 学科类型名称
     */
    @JSONField(name = "CourseTypeName")
    private String courseTypeName;
    
    /**
     * 排序
     */
    @JSONField(name = "OrderIndex")
    private Integer orderIndex;

    /**
     * 默认学段年级(小学-7 初中-1 高中-4)
     */

    @JSONField(name = "DefaultGradeId")
    private Integer defaultGradeId;


}
