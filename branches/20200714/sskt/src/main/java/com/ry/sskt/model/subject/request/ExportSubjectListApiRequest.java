package com.ry.sskt.model.subject.request;

import com.ry.sskt.model.common.request.RequestBase;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 【课堂管理】导出课程数据 输入参数
 */
@Data
@Accessors(chain = true)
public class ExportSubjectListApiRequest extends RequestBase {
    /// <summary>
    /// 需要下载的课程加密Id列表
    /// </summary>
    @NotNull
    @Size(min = 1)
    private List<String> SubjectIds;

}
