package com.ry.sskt.model.subject.response;

import com.ry.sskt.model.subject.entity.GetCurrentSubjectNameDetail;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

/**
 * 【课堂管理】获取课程详情 输出参数
 */
@Data
@Accessors(chain = true)
public class GetSubjectVideoResponse {
    /// <summary>
    /// 课程加密Id
    /// </summary>
    private String SubjectId;

    /// <summary>
    /// 课程加密Id,可以为空
    /// </summary>
    private String SubjectName;

    /// <summary>
    /// 主讲老师加密ID
    /// </summary>
    private String TeacherId;

    /// <summary>
    /// 主讲老师名称
    /// </summary>
    private String TeacherName;

    /// <summary>
    /// 助教老师加密ID
    /// </summary>
    private String HelperTeacherId;

    /// <summary>
    /// 播放地址
    /// </summary>
    private List<String> VideoUrl;

    /// <summary>
    /// 播放地址
    /// </summary>
    private List<String> PPTVideoUrl;

    /// <summary>
    /// 随堂练习ID
    /// </summary>
    private String ExamSetId;

    /// <summary>
    /// 课程状态
    /// </summary>
    private int SubjectStatus;

    /// <summary>
    /// 作业是否已经完成
    /// </summary>
    private boolean IsFinish;

    private Integer channel;

    private Integer uid;

    /// <summary>
    /// 构造函数
    /// </summary>
    public GetSubjectVideoResponse() {
        VideoUrl = new LinkedList<String>();
        PPTVideoUrl = new LinkedList<String>();
    }
}
