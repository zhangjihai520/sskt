package com.ry.sskt.model.subject.response;

import com.ry.sskt.model.subject.entity.GetCurrentSubjectNameDetail;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

/**
 * 【课程统计/学情监控】获取热门数据 输出参数
 */
@Data
@Accessors(chain = true)
public class SaveGroupByStudentListResponse {
    /// <summary>
    /// 0失败 1成功 2达到助教老师人数上限
    /// </summary>
    private int StatusFlag;

    /// <summary>
    /// 助教分组人数还剩几名
    /// </summary>
    private int Number;
}
