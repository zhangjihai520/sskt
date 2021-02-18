package com.ry.sskt.model.subject.request;

import com.ry.sskt.model.common.request.RequestBase;
import com.ry.sskt.model.subject.entity.StudentRoom;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 搜索课程
 */
@Data
@Accessors(chain = true)
public class SearchSubjectRequest extends RequestBase {
    /// <summary>
    /// 关键字 课程名称或老师名称
    /// </summary>
    private String Key;
}
