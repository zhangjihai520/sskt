package com.ry.sskt.model.subject.request;

import com.ry.sskt.model.common.request.RequestBase;
import com.ry.sskt.model.subject.entity.SubjectRoomDetail;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 【课表管理】添加课程 输入参数
 */
@Data
@Accessors(chain = true)
public class EditSubjectApiRequest extends RequestBase {
    /// <summary>
    /// 课程加密Id,可以为空
    /// </summary>
    private String SubjectId;

    /// <summary>
    /// 课程加密Id,可以为空
    /// </summary>
    private String SubjectName;

    /// <summary>
    /// 报名开始时间
    /// </summary>
    private LocalDateTime RegistBeginTime;

    /// <summary>
    /// 报名截止时间
    /// </summary>
    private LocalDateTime RegistEndTime;

    /// <summary>
    /// 课程开始时间
    /// </summary>
    private LocalDateTime BeginTime;

    /// <summary>
    /// 课程结束时间
    /// </summary>
    private LocalDateTime EndTime;

    /// <summary>
    /// 所属学科枚举值
    /// </summary>
    private int CourseId;

    /// <summary>
    /// 年级枚举值
    /// </summary>
    private int GradeId;

    /// <summary>
    /// 教师Id
    /// </summary>
    private String TeacherId;

    /// <summary>
    /// 教师名称
    /// </summary>
    private String TeacherName;

    /// <summary>
    /// 图片路径
    /// </summary>
    private String ImagePath;

    /// <summary>
    /// 备注
    /// </summary>
    private String Comment;

    /// <summary>
    /// 公益课录像地址
    /// </summary>
    private String VideoUrl;

    /// <summary>
    /// 课程类型'课程类型0全部   1、普通课程 2、公益课课程'
    /// </summary>
    private int SubjectGenreId;

    /// <summary>
    /// 教室列表
    /// </summary>
    private List<SubjectRoomDetail> SubjectRooms;

    /// <summary>
    /// 构造函数
    /// </summary>
    public EditSubjectApiRequest() {
        SubjectGenreId = 1;
    }

}
