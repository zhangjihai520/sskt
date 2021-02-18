package com.ry.sskt.model.subject.response;

import com.ry.sskt.core.annotation.ExcelColumn;
import com.ry.sskt.model.subject.entity.DateGroupDetail;
import com.ry.sskt.model.subject.entity.GetSubjectListDetail;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

/**
 * 【课表管理】获取课程列表返回参数列表
 */
@Data
@Accessors(chain = true)
public class SubjectExport {

    @ExcelColumn(value = "课程名称", col = 1)
    private String SubjectName;

    @ExcelColumn(value = "年级", col = 2)
    private String GradeName;

    @ExcelColumn(value = "学科学段", col = 3)
    private String CourseName;

    @ExcelColumn(value = "教师名称", col = 4)
    private String TeacherNameList;

    @ExcelColumn(value = "课程时间", col = 5)
    private String BeginTime;

    @ExcelColumn(value = "课程状态", col = 6)
    private String SubjectStatus;

    public SubjectExport() {
    }

    public SubjectExport(String subjectName, String gradeName, String courseName, String teacherNameList, String beginTime, String subjectStatus) {
        SubjectName = subjectName;
        GradeName = gradeName;
        CourseName = courseName;
        TeacherNameList = teacherNameList;
        BeginTime = beginTime;
        SubjectStatus = subjectStatus;
    }
}
