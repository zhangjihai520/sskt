package com.ry.sskt.model.subject.entity.view;

import com.alibaba.fastjson.annotation.JSONField;
import io.protostuff.Tag;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Data
@Accessors(chain = true)
public class SubjectListByFilterView {
    /// <summary>
    /// 课程id
    /// </summary>

    private int SubjectId;

    /// <summary>
    /// 课程名称
    /// </summary>

    private String SubjectName;

    /// <summary>
    /// 报名开始时间
    /// </summary>
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")

    private LocalDateTime RegistBeginTime;

    /// <summary>
    /// 报名截止时间
    /// </summary>
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")

    private LocalDateTime RegistEndTime;

    /// <summary>
    /// 课程开始时间
    /// </summary>
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")

    private LocalDateTime BeginTime;

    /// <summary>
    /// 课程结束时间
    /// </summary>
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")

    private LocalDateTime EndTime;

    /// <summary>
    /// 适用年级
    /// </summary>

    private int GradeId;

    /// <summary>
    /// 学段学科
    /// </summary>

    private int CourseId;

    /// <summary>
    /// 主讲老师编号
    /// </summary>

    private int TeacherId;

    /// <summary>
    /// 图片路径
    /// </summary>

    private String ImagePath;

    /// <summary>
    /// 课程描述
    /// </summary>

    private String Comment;

    /// <summary>
    /// 课程状态，0删除，1待上架、2已上架 <see cref="VideoClass.Model.Enum.SubjectStatusFlagEnum"/>
    /// </summary>

    private int StatusFlag;

    /// <summary>
    /// 教室类型，0线上，1线下
    /// </summary>

    private int SubjectTypeId;

    /// <summary>
    /// 创建时间
    /// </summary>
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")

    private LocalDateTime CreateDateTime;

    /// <summary>
    /// 更新时间
    /// </summary>
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")

    private LocalDateTime UpdateDateTime;

    /// <summary>
    /// 直播房间号
    /// </summary>

    private String VideoRoom;

    /// <summary>
    /// 上课状态，1待上课,2上课中,3已下课,4手动下课
    /// </summary>

    private int ClassStateId;

    /// <summary>
    /// 播放地址
    /// </summary>

    private String FilePath;

    /// <summary>
    /// 播放地址(第二路)
    /// </summary>

    private String PPTFilePath;

    /// <summary>
    /// 课程类型 1、普通课程 2、公益课课程
    /// </summary>

    private int SubjectGenreId;


    /// <summary>
    /// 关联老师名称，用,相连
    /// </summary>

    private String TeacherNameList;

    /// <summary>
    /// 教室ID
    /// </summary>

    private int SubjectRoomId;

    public boolean isForenoon() {
        return getBeginTime().getHour() < 12;
    }

    public String BeginTimeFomatToLocalDate() {
        return BeginTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
