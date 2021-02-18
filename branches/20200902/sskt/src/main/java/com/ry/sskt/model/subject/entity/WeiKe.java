package com.ry.sskt.model.subject.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 微课
 */
@Data
@Accessors(chain = true)
public class WeiKe {
    /// <summary>
    /// 微课id
    /// </summary>
    @JSONField(name="WeiKeId")
    private int weiKeId;

    /// <summary>
    /// 微课名称
    /// </summary>
    @JSONField(name="WeiKeName")
    private String weiKeName;

    /// <summary>
    /// 上传老师编号
    /// </summary>
    @JSONField(name="TeacherId")
    private int teacherId;

    /// <summary>
    /// 适用年级
    /// </summary>
    @JSONField(name="GradeId")
    private int gradeId;

    /// <summary>
    /// 学段学科
    /// </summary>
    @JSONField(name="CourseId")
    private int courseId;

    /// <summary>
    /// 文件路径，json格式存储
    /// </summary>
    @JSONField(name="FilePath")
    private String filePath;

    /// <summary>
    /// 课程描述
    /// </summary>
    @JSONField(name="Comment")
    private String comment;

    /// <summary>
    /// 教材版本
    /// </summary>
    @JSONField(name="BookVersionId")
    private int bookVersionId;

    /// <summary>
    /// 课本
    /// </summary>
    @JSONField(name="CourseMappingId")
    private int courseMappingId;

    /// <summary>
    /// 章节
    /// </summary>
    @JSONField(name="SectionId")
    private int sectionId;

    /// <summary>
    /// 课程状态，0删除，1待上架、2已上架（待审核）、3已上架、4已驳回
    /// <see cref="WeiKeStatusFlagEnum"/>
    /// </summary>
    @JSONField(name="StatusFlag")
    private int statusFlag;

    /// <summary>
    /// 创建时间
    /// </summary>
    @JSONField(name="CreateDateTime")
    private LocalDateTime createDateTime;

    /// <summary>
    /// 更新时间
    /// </summary>
    @JSONField(name="UpdateDateTime")
    private LocalDateTime updateDateTime;

    /// <summary>
    /// 封面地址
    /// </summary>
    @JSONField(name="OverViewUrl")
    private String overViewUrl;

    /// <summary>
    /// 微课录像地址(PPT)第二路视频
    /// </summary>
    @JSONField(name="PPTFilePath")
    private String pptFilePath;

    /// <summary>
    /// 对应录像房间, 如果为0,表示上传的录像
    /// </summary>
    @JSONField(name="SourceRoomId")
    private String sourceRoomId;

    /// <summary>
    /// 冗余字段,不在数据库中
    /// </summary>
    @JSONField(name="TeacherName")
    private String teacherName;
}
