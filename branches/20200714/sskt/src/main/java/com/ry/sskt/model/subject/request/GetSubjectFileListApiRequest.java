package com.ry.sskt.model.subject.request;

import com.ry.sskt.model.common.request.RequestBase;
import com.ry.sskt.model.common.request.RequestPageBase;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 【课件资料】课件资料列表 输入参数
 */
@Data
@Accessors(chain = true)
public class GetSubjectFileListApiRequest extends RequestPageBase {
    /// <summary>
    /// 课程加密Id
    /// </summary>
    private String SubjectId;

    /// <summary>
    /// 资料类型 不传为全部
    /// </summary>
    private List<String> FileType;

    /// <summary>
    /// 资料名称 不传为全部
    /// </summary>
    private String FileName;
}
