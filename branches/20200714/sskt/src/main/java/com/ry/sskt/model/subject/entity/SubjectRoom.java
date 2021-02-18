package com.ry.sskt.model.subject.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class SubjectRoom {
    /// <summary>
    /// 教室id
    /// </summary>
    @JSONField(name = "SubjectRoomId")
    private Integer subjectRoomId;

    /// <summary>
    /// 课程id
    /// </summary>
    @JSONField(name = "SubjectId")
    private int subjectId;

    /// <summary>
    /// 助教老师编号
    /// </summary>
    @JSONField(name = "TeacherId")
    private int teacherId;

    /// <summary>
    /// 教室名称
    /// </summary>
    @JSONField(name = "SubjectRoomName")
    private String subjectRoomName;

    /// <summary>
    /// 学校名
    /// </summary>
    @JSONField(name = "SchoolName")
    private String schoolName;

    /// <summary>
    /// 最大报名数
    /// </summary>
    @JSONField(name = "MaxRegisterNumber")
    private int maxRegisterNumber;

    /// <summary>
    /// 报名人数
    /// </summary>
    @JSONField(name = "RealRegisterNumber")
    private int realRegisterNumber;

    /// <summary>
    /// 上课人数
    /// </summary>
    @JSONField(name = "AttendNumber")
    public int attendNumber;

    /// <summary>
    /// 课程状态，0删除，1未删除
    /// </summary>
    @JSONField(name = "StatusFlag")
    private int statusFlag;

    /// <summary>
    /// 创建时间
    /// </summary>
    @JSONField(name = "CreateDateTime", format = "yyyy-MM-dd HH:mm:ss")
    private Date createDateTime;

    /// <summary>
    /// 更新时间
    /// </summary>
    @JSONField(name = "UpdateDateTime", format = "yyyy-MM-dd HH:mm:ss")
    private Date updateDateTime;
}
