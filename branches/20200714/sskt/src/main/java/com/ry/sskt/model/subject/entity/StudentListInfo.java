package com.ry.sskt.model.subject.entity;

import com.ry.sskt.core.utils.UrlUtil;
import com.ry.sskt.model.common.constant.CommonConst;
import com.ry.sskt.model.student.entity.view.StudentListByGroupView;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

/**
 * 学生信息
 */
@Data
@Accessors(chain = true)
public class StudentListInfo {
    /// <summary>
    /// 学生Id
    /// </summary>
    private int UserId;

    /// <summary>
    /// 学生名称
    /// </summary>
    private String Name;

    /// <summary>
    /// 报名时间 2017/11/01 18:00:00
    /// </summary>
    private String RegisterTime;

    /// <summary>
    /// 教师名字
    /// </summary>
    private String TeacherName;

    /// <summary>
    /// 教室ID
    /// </summary>
    private String SubjectRoomId;

    /// <summary>
    /// 获取实例
    /// </summary>
    /// <param name="view"></param>
    /// <returns></returns>
    public StudentListInfo(StudentListByGroupView view)
    {
            UserId = view.getUserId();
            Name = view.getUserTrueName();
            RegisterTime = view.getCreateDateTime().format(CommonConst.datefomat_nomal2);
            TeacherName = view.getTeacherName();
            SubjectRoomId = UrlUtil.encrypt(view.getSubjectRoomId());
    }
}
