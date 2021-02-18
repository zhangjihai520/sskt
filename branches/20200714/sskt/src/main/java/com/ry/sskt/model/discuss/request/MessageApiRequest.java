package com.ry.sskt.model.discuss.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.common.request.RequestBase;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MessageApiRequest extends RequestBase {
    /// <summary>
    /// 课程加密Id
    /// </summary>
    @JSONField(name = "SubjectId")
    private String subjectId;

    /// <summary>
    /// 1代表获取旧数据，2代表获取新数据,3.获取最新数据
    /// </summary>
    @JSONField(name = "TypeId")
    private int typeId;

    /// <summary>
    /// 最新Id
    /// </summary>
    @JSONField(name = "BoundaryId")
    private int boundaryId;

    /// <summary>
    /// 页数
    /// </summary>
    @JSONField(name = "PageSize")
    private int pageSize;
}
