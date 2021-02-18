package com.ry.sskt.service.impl;

import com.ry.sskt.core.utils.RedisUtil;
import com.ry.sskt.mapper.GradeCourseMapper;
import com.ry.sskt.model.common.constant.CommonConst;
import com.ry.sskt.model.common.entity.Course;
import com.ry.sskt.model.common.entity.CourseType;
import com.ry.sskt.model.common.entity.Grade;
import com.ry.sskt.model.common.entity.cache.CourseCache;
import com.ry.sskt.model.common.entity.cache.CourseTypeCache;
import com.ry.sskt.model.common.entity.cache.GradeCache;
import com.ry.sskt.service.IGradeCourseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统配置表 服务实现类
 * </p>
 *
 * @author xrq
 * @since 2020-03-23
 */
@Service
@Slf4j
public class GradeCourseService implements IGradeCourseService {

    @Autowired
    GradeCourseMapper gradeCourseMapper;

    @Override
    public List<Course> getAllCourse(boolean needFilterDeletedCourses) {
        CourseCache courseCache = RedisUtil.getObj(new CourseCache().getKey(), CourseCache.class);
        if (null == courseCache || CollectionUtils.isEmpty(courseCache.getCourses())) {
            List<Course> courses = gradeCourseMapper.getAllCourse();
            if (CollectionUtils.isNotEmpty(courses)) {
                courseCache = new CourseCache().setCourses(courses);
                RedisUtil.setObj(courseCache, CommonConst.MINUTE_120);
            }
            return courses;
        }
        List<Course> courses = courseCache.getCourses();
        if (needFilterDeletedCourses) {
            return courses;
        }
        return courses.stream().filter(x -> null != x.getStatusFlag() && x.getStatusFlag().equals(1)).collect(Collectors.toList());
    }

    @Override
    public List<Grade> getAllGrade() {
        GradeCache gradeCache = RedisUtil.getObj(new GradeCache().getKey(), GradeCache.class);
        if (null == gradeCache || CollectionUtils.isEmpty(gradeCache.getGrades())) {
            List<Grade> grades = gradeCourseMapper.getAllGrade();
            if (CollectionUtils.isNotEmpty(grades)) {
                gradeCache = new GradeCache().setGrades(grades);
                RedisUtil.setObj(gradeCache, CommonConst.MINUTE_120);
            }
            return grades;
        }
        return gradeCache.getGrades();
    }

    @Override
    public List<CourseType> getAllCourseType() {
        CourseTypeCache courseTypeCache = RedisUtil.getObj(new CourseTypeCache().getKey(), CourseTypeCache.class);
        if (null == courseTypeCache || CollectionUtils.isEmpty(courseTypeCache.getCourseTypes())) {
            List<CourseType> courses = gradeCourseMapper.getAllCourseType();
            if (CollectionUtils.isNotEmpty(courses)) {
                courseTypeCache = new CourseTypeCache().setCourseTypes(courses);
                RedisUtil.setObj(courseTypeCache, CommonConst.MINUTE_120);
            }
            return courses;
        }
        return courseTypeCache.getCourseTypes();
    }

    @Override
    public List<Course> getStudentCourseByGradeId(int gradeId) {

        Grade courseType = getAllGrade().stream().filter(x -> x.getGradeId() == gradeId).findFirst().orElse(null);
        if (courseType == null) {
            return new LinkedList<Course>();
        }
        List<Course> course = getAllCourse(true).stream().filter(x -> x.getCourseTypeId() == courseType.getCourseTypeId()).collect(Collectors.toList());
        return course;
    }
}
