package com.ry.sskt.model.subject.request;

import com.ry.sskt.model.common.request.RequestBase;
import com.ry.sskt.model.common.request.RequestPageBase;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 *【课表管理】下架/上架/删除课程 输入参数
 */
@Data
@Accessors(chain = true)
public class UpdateSubjectStatusApiRequest extends RequestBase {
    /// <summary>
    /// 课程加密Id
    /// </summary>
    private String SubjectId;

    /// <summary>
    /// 操作状态 <see cref="UpdateSubjectStatusEnum"/> 0=上架,1=下架,2=删除
    /// </summary>
    private int StatusFlag;

}
