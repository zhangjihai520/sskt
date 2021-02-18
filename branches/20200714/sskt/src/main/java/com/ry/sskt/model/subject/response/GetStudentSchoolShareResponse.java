package com.ry.sskt.model.subject.response;

import com.ry.sskt.model.subject.entity.PieDataDetail;
import com.ry.sskt.model.subject.entity.SubjectRoomDetail;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

/**
 * 【学情监控】获取学生学校占数比 输出参数
 */
@Data
@Accessors(chain = true)
public class GetStudentSchoolShareResponse {
    /// <summary>
    /// 总数
    /// </summary>
    private int ValueCount;

    /// <summary>
    /// 饼图数据
    /// </summary>
    private List<PieDataDetail> PieData;

    /// <summary>
    /// 构造函数
    /// </summary>
    public GetStudentSchoolShareResponse() {
        PieData = new LinkedList<PieDataDetail>();
    }
}
