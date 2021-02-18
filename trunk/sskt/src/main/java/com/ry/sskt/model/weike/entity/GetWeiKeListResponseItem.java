package com.ry.sskt.model.weike.entity;

import com.ry.sskt.core.utils.UrlUtil;
import com.ry.sskt.model.common.entity.Course;
import com.ry.sskt.model.common.entity.Grade;
import com.ry.sskt.model.common.entity.IdName;
import com.ry.sskt.model.subject.entity.WeiKe;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@Accessors(chain = true)
public class GetWeiKeListResponseItem {
    // <summary>
    /// 微课的Id
    /// </summary>
    private String WeiKeId;

    /// <summary>
    /// 微课名称
    /// </summary>
    private String WeiKeName;

    /// <summary>
    /// 适用年级
    /// </summary>
    private String GradeName;

    /// <summary>
    /// 学段学科
    /// </summary>
    private String CourseName;

    /// <summary>
    /// 学段短码学科
    /// </summary>
    private String ShortCode;

    /// <summary>
    /// 教材版本
    /// </summary>
    private String BookVersionName;

    /// <summary>
    /// 课本
    /// </summary>
    private String CourseMappingName;

    /// <summary>
    /// 章节
    /// </summary>
    private String SectionName;

    /// <summary>
    /// 课程状态，0删除，1待上架、2已上架
    /// </summary>
    private int StatusFlag;

    /// <summary>
    /// 封面地址
    /// </summary>
    private String OverViewUrl;

    /// <summary>
    /// 创建人名称
    /// </summary>
    private String CreateUserName;

    /// <summary>
    /// 创建时间
    /// </summary>
    private String CreateDateTime;

    /// <summary>
    /// 是否可以上架,1=可以,其他=不可以
    /// </summary>
    private int CanActive;

    /// <summary>
    /// 设备ID
    /// </summary>
    private String DeviceId;

    public GetWeiKeListResponseItem() {
    }

    /// <summary>
    /// 获取微课详情
    /// </summary>
    /// <param name="weike"></param>
    /// <returns></returns>
    public GetWeiKeListResponseItem(WeiKe weike, List<IdName> bookVersions, List<IdName> courseMappings, List<IdName> sections, List<Course> courses, List<Grade> grades) {

        if (weike.getBookVersionId() != 999999999) {
            BookVersionName = bookVersions.stream().filter(m -> m.getId().equals(weike.getBookVersionId() + "")).findFirst().orElse(new IdName()).getName();
            CourseMappingName = courseMappings.stream().filter(m -> m.getId().equals(weike.getCourseMappingId() + "")).findFirst().orElse(new IdName()).getName();
            SectionName = sections.stream().filter(m -> m.getId().equals(weike.getSectionId() + "")).findFirst().orElse(new IdName()).getName();
        } else {
            BookVersionName = "复习专题课";
            CourseMappingName = "复习专题课";
            SectionName = "复习专题课";
        }
        Course course = courses.stream().filter(m -> m.getCourseId() == weike.getCourseId()).findFirst().orElse(new Course());
        CourseName = course.getCourseName();
        ShortCode = course.getShortCode();
        GradeName = grades.stream().filter(m -> m.getGradeId() == weike.getGradeId()).findFirst().orElse(new Grade()).getGradeName();
        StatusFlag = weike.getStatusFlag();
        WeiKeId = UrlUtil.encrypt(weike.getWeiKeId());
        WeiKeName = weike.getWeiKeName();
        OverViewUrl = weike.getOverViewUrl();
        CreateDateTime = weike.getCreateDateTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        CreateUserName = weike.getTeacherName();
        CanActive = StringUtils.isNotBlank(weike.getFilePath()) && weike.getFilePath().length() > 2 ? 1 : 0;
        DeviceId = StringUtils.isBlank(weike.getSourceRoomId()) ? StringUtils.EMPTY : weike.getSourceRoomId().split("_")[0];
    }
}
