package com.ry.sskt.model.subject.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.core.annotation.IRedisStoredObject;
import io.protostuff.Tag;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Accessors(chain = true)
public class Subject implements IRedisStoredObject {
    private static String key = "Subject:SubjectId_%s";

    @Override
    public String getKey() {
        return String.format(key, this.subjectId);
    }
    /// <summary>
    /// 课程id
    /// </summary>

    @JSONField(name = "SubjectId")
    private int subjectId;

    /// <summary>
    /// 课程名称
    /// </summary>

    @JSONField(name = "SubjectName")
    private String subjectName;

    /// <summary>
    /// 报名开始时间
    /// </summary>

    @JSONField(name = "RegistBeginTime")
    private LocalDateTime registBeginTime;

    /// <summary>
    /// 报名截止时间
    /// </summary>

    @JSONField(name = "RegistEndTime")
    private LocalDateTime registEndTime;

    /// <summary>
    /// 课程开始时间
    /// </summary>

    @JSONField(name = "BeginTime")
    private LocalDateTime beginTime;

    /// <summary>
    /// 课程结束时间
    /// </summary>

    @JSONField(name = "EndTime")
    private LocalDateTime endTime;

    /// <summary>
    /// 适用年级
    /// </summary>

    @JSONField(name = "GradeId")
    private int gradeId;

    /// <summary>
    /// 学段学科
    /// </summary>

    @JSONField(name = "CourseId")
    private int courseId;

    /// <summary>
    /// 主讲老师编号
    /// </summary>

    @JSONField(name = "TeacherId")
    private int teacherId;

    /// <summary>
    /// 图片路径
    /// </summary>

    @JSONField(name = "ImagePath")
    private String imagePath;

    /// <summary>
    /// 课程描述
    /// </summary>

    @JSONField(name = "Comment")
    private String comment;

    /// <summary>
    /// 课程状态，0删除，1待上架、2已上架 <see cref="VideoClass.Model.Enum.SubjectStatusFlagEnum"/>
    /// </summary>

    @JSONField(name = "StatusFlag")
    private int statusFlag;

    /// <summary>
    /// 教室类型，0线上，1线下
    /// </summary>

    @JSONField(name = "SubjectTypeId")
    private int subjectTypeId;

    /// <summary>
    /// 创建时间
    /// </summary>

    @JSONField(name = "CreateDateTime")
    private LocalDateTime createDateTime;

    /// <summary>
    /// 更新时间
    /// </summary>

    @JSONField(name = "UpdateDateTime")
    private LocalDateTime updateDateTime;

    /// <summary>
    /// 直播房间号
    /// </summary>

    @JSONField(name = "VideoRoom")
    private String videoRoom;

    /// <summary>
    /// 上课状态，1待上课,2上课中,3已下课,4手动下课
    /// </summary>

    @JSONField(name = "ClassStateId")
    private int classStateId;

    /// <summary>
    /// 播放地址
    /// </summary>

    @JSONField(name = "FilePath")
    private String filePath;

    /// <summary>
    /// 播放地址(第二路)
    /// </summary>

    @JSONField(name = "PPTFilePath")
    private String pptFilePath;

    /// <summary>
    /// 课程类型 1、普通课程 2、公益课课程
    /// </summary>
    @JSONField(name = "SubjectGenreId")
    private int subjectGenreId;

    @JSONField(name = "RealBeginTimeZone")
    private Long realBeginTimeZone;
}
