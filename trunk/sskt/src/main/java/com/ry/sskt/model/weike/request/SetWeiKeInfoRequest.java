package com.ry.sskt.model.weike.request;

import com.alibaba.fastjson.JSONArray;
import com.ry.sskt.core.utils.UrlUtil;
import com.ry.sskt.model.common.request.RequestBase;
import com.ry.sskt.model.subject.entity.WeiKe;
import com.ry.sskt.model.video.entity.VHVideoView;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * 读取微课请求
 */
@Data
@Accessors(chain = true)
public class SetWeiKeInfoRequest extends RequestBase {
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
    /// 图片地址
    /// </summary>
    private String OverViewUrl;

    /// <summary>
    /// 获取微课详情
    /// </summary>
    /// <param name="weike"></param>
    /// <returns></returns>
    public WeiKe ConvertToWeiKe(WeiKe oldMod) {
        String fp = "";
        List<VHVideoView> vls = new LinkedList<>();
        if (CollectionUtils.isEmpty(VideoUrl)) {
            fp = StringUtils.EMPTY;
        } else {
            VideoUrl.forEach(x -> {
                vls.add(new VHVideoView().setUrl(x.getUrl()).setFilename(x.getFilename()));
            });
            fp = JSONArray.toJSONString(vls);
        }
        return new WeiKe().setBookVersionId(this.BookVersionId == 0 && oldMod != null ? oldMod.getBookVersionId() : this.BookVersionId)
                .setCourseId(this.CourseId == 0 && oldMod != null ? oldMod.getCourseId() : this.CourseId)
                .setCourseMappingId(this.CourseMappingId == 0 && oldMod != null ? oldMod.getCourseMappingId() : this.CourseMappingId)
                .setGradeId(this.GradeId == 0 && oldMod != null ? oldMod.getGradeId() : this.GradeId)
                .setSectionId(this.SectionId == 0 && oldMod != null ? oldMod.getSectionId() : this.SectionId)
                .setFilePath(fp)
                .setWeiKeId(UrlUtil.decrypt(this.WeiKeId, Integer.class))
                .setWeiKeName(StringUtils.isNotEmpty(this.WeiKeName) ? this.WeiKeName : StringUtils.EMPTY)
                .setStatusFlag(oldMod != null ? oldMod.getStatusFlag() : 1)
                .setComment(this.Remarks)
                .setOverViewUrl(StringUtils.isEmpty(this.OverViewUrl) ? StringUtils.EMPTY : this.OverViewUrl)
                .setTeacherId(this.getUserId())
                .setPptFilePath(oldMod == null ? StringUtils.EMPTY : oldMod.getPptFilePath())
                .setCreateDateTime(oldMod == null ? LocalDateTime.now() : oldMod.getCreateDateTime())
                .setUpdateDateTime(LocalDateTime.now())
                .setSourceRoomId(oldMod == null ? StringUtils.EMPTY : oldMod.getSourceRoomId());
    }
}
