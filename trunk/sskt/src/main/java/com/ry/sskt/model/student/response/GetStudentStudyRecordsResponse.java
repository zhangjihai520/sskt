package com.ry.sskt.model.student.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.student.entity.StudentStudyRecordInfo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

/**
 * 获取学生轨迹Response
 */
@Data
@Accessors(chain = true)
public class GetStudentStudyRecordsResponse {

    /// <summary>
    /// 头像
    /// </summary>
    private String UserFace;

    /// <summary>
    /// 学生名称
    /// </summary>
    private String UserTrueName;

    /// <summary>
    /// 学生Id,加密
    /// </summary>
    private String UserId;

    /// <summary>
    /// 学校名称
    /// </summary>
    private String SchoolName;

    /// <summary>
    /// 年级
    /// </summary>
    private String GradeName;

    /// <summary>
    /// 学生轨迹列表
    /// </summary>
    private List<StudentStudyRecordInfo> StudentStudyRecordList;

    public GetStudentStudyRecordsResponse() {
        StudentStudyRecordList = new LinkedList<StudentStudyRecordInfo>();
    }
}
