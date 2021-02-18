package com.ry.sskt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ry.sskt.model.common.entity.*;

import java.util.List;

/**
 * <p>
 * GradeCourseService对应的接口
 * </p>
 *
 * @author xrq
 * @since 2020-03-23
 */
public interface IGradeCourseService {

    /**
     * 获取所有的学科
     * @param needFilterDeletedCourses 是否需要过滤掉不可用的学科(默认为true)
     * @return 所有的学科
     */
    List<Course> getAllCourse(boolean needFilterDeletedCourses);

    /**
     * 获取所有年级
     * @return 所有年级
     */
    List<Grade> getAllGrade();

    /**
     * 获取所有的学科类型
     * @return 所有的学科类型
     */
    List<CourseType> getAllCourseType();

    /**
     * 根据年级Id获取学生的课程信息
     * @param gradeId 年级id
     * @return 课程信息列表
     */
    List<Course> getStudentCourseByGradeId(int gradeId);
}