package com.ry.sskt.model.teacher.entity;

import com.ry.sskt.core.utils.UrlUtil;
import com.ry.sskt.model.student.entity.view.GetAllSubjectEvaluateView;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.format.DateTimeFormatter;

/// <summary>
/// 消息
/// </summary>
@Data
@Accessors(chain = true)
public class SubjectEvaluateInfo {
    /// <summary>
    /// 课程加密Id
    /// </summary>
    private String SubjectId;

    /// <summary>
    /// 课程名称
    /// </summary>
    private String SubjectName;

    /// <summary>
    /// 课程开课时间
    /// </summary>
    private String BeginTime;

    /// <summary>
    /// 评价数量
    /// </summary>
    private int EvaluateCount;

    /// <summary>
    /// 获取实例
    /// </summary>
    /// <returns></returns>
    public SubjectEvaluateInfo() {
    }

    public SubjectEvaluateInfo(GetAllSubjectEvaluateView view) {

        SubjectId = UrlUtil.encrypt(view.getSubjectId());
        SubjectName = view.getSubjectName();
        BeginTime = view.getBeginTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        EvaluateCount = view.getEvaluateCount();

    }
}
