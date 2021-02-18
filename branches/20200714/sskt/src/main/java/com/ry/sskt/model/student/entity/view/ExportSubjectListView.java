package com.ry.sskt.model.student.entity.view;

import com.ry.sskt.model.subject.entity.Subject;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

/// <summary>
/// 导出课程输出对象
/// </summary>
@Data
@Accessors(chain = true)
public class ExportSubjectListView extends Subject {
    /// <summary>
    /// 老师列表 主教，助教
    /// </summary>
    private String TeacherNameList;

    /// <summary>
    /// 获取课程实例
    /// </summary>
    /// <param name="exportSubjectListView"></param>
    /// <returns></returns>
    public static Subject GetSubjectInstance(ExportSubjectListView exportSubjectListView) {
        return new Subject()
                .setSubjectId(exportSubjectListView.getSubjectId())
                .setSubjectName(exportSubjectListView.getSubjectName())
                .setRegistBeginTime(exportSubjectListView.getRegistBeginTime())
                .setRegistEndTime(exportSubjectListView.getRegistEndTime())
                .setBeginTime(exportSubjectListView.getBeginTime())
                .setEndTime(exportSubjectListView.getEndTime())
                .setGradeId(exportSubjectListView.getGradeId())
                .setCourseId(exportSubjectListView.getCourseId())
                .setStatusFlag(exportSubjectListView.getStatusFlag())
                .setSubjectTypeId(exportSubjectListView.getSubjectTypeId())
                .setCreateDateTime(exportSubjectListView.getCreateDateTime())
                .setUpdateDateTime(exportSubjectListView.getUpdateDateTime());
    }
}
