package com.ry.sskt.model.subject.request;

import com.ry.sskt.model.common.request.RequestBase;
import com.ry.sskt.model.common.request.RequestPageBase;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 获取我的学生课程列表 输入参数
 */
@Data
@Accessors(chain = true)
public class GetStudentSubjectListApiRequest extends RequestPageBase {
    /// <summary>
    /// 学科Id
    /// </summary>
    private int CourseId;

    /// <summary>
    /// 学科Id
    /// </summary>
    private int GradeId;

    /// <summary>
    /// 未开始 = 0  已开始 = 1 已结课 = 2<see cref="GetStudentSubjectListEnum"/>
    /// </summary>
    private int TypeEnum;

    /// <summary>
    /// 是否获取我的学科
    /// </summary>
    private int IsMySubject;

    /// <summary>
    /// 搜索关键字
    /// </summary>
    private String Keyword;

    /// <summary>
    /// 课程类型
    /// </summary>
    private int SubjectGenreId;

}
