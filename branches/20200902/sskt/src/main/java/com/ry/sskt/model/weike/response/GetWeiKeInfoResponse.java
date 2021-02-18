package com.ry.sskt.model.weike.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.core.utils.UrlUtil;
import com.ry.sskt.model.subject.entity.WeiKe;
import com.ry.sskt.model.video.entity.VHVideoView;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class GetWeiKeInfoResponse {

    /// <summary>
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
    private int GradeId;

    /// <summary>
    /// 学段学科
    /// </summary>
    private int CourseId;

    /// <summary>
    /// 文件路径，json格式存储["",""] 这样的格式
    /// </summary>
    private List<VHVideoView> VideoUrl;

    /// <summary>
    /// 播放地址，json格式存储["",""] 这样的格式
    /// </summary>
    private List<VHVideoView> PPTVideoUrl;

    /// <summary>
    /// 课程描述
    /// </summary>
    private String Remarks;

    /// <summary>
    /// 教材版本
    /// </summary>
    private int BookVersionId;

    /// <summary>
    /// 课本
    /// </summary>
    private int CourseMappingId;

    /// <summary>
    /// 章节
    /// </summary>
    private int SectionId;

    /// <summary>
    /// 章节名称
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
    /// 封面地址
    /// </summary>
    private String CreateUserName;

    public GetWeiKeInfoResponse() {
    }

    public GetWeiKeInfoResponse(WeiKe weike, List<VHVideoView> videourl, List<VHVideoView> pptVideorul) {

        BookVersionId = weike.getBookVersionId();
        CourseId = weike.getCourseId();
        CourseMappingId = weike.getCourseMappingId();
        GradeId = weike.getGradeId();
        Remarks = weike.getComment();
        SectionId = weike.getSectionId();
        StatusFlag = weike.getStatusFlag();
        VideoUrl = videourl;
        PPTVideoUrl = pptVideorul;
        WeiKeId = UrlUtil.encrypt(weike.getWeiKeId());
        WeiKeName = weike.getWeiKeName();
        OverViewUrl = weike.getOverViewUrl();
        CreateUserName = weike.getTeacherName();
    }
}
