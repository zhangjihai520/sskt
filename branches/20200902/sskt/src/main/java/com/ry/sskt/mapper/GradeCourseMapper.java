package com.ry.sskt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ry.sskt.model.common.entity.Course;
import com.ry.sskt.model.common.entity.CourseType;
import com.ry.sskt.model.common.entity.Grade;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

/**
 * <p>
 * 学科表 Mapper 接口
 * </p>
 *
 * @author xrq
 * @since 2020-04-17
 */
public interface GradeCourseMapper {

    /**
     * 获取所有学科
     * @return 所有学科
     */
    @Select("call VideoClassMain_SP_Course_ReadAll()")
    @Options(statementType = StatementType.CALLABLE)
    List<Course> getAllCourse();

    /**
     * 获取所有的学科类型
     * @return 所有的学科类型
     */
    @Select("call VideoClassMain_SP_CourseType_ReadAll()")
    @Options(statementType = StatementType.CALLABLE)
    List<CourseType> getAllCourseType();

    /**
     * 获取所有的年级
     * @return 所有的年级
     */
    @Select("call VideoClassMain_SP_Grade_ReadAll()")
    @Options(statementType = StatementType.CALLABLE)
    List<Grade> getAllGrade();
}
