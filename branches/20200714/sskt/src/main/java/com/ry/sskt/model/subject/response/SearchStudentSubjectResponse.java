package com.ry.sskt.model.subject.response;

import com.ry.sskt.model.subject.entity.GetHotSubjectListDetail;
import com.ry.sskt.model.subject.entity.GetUserSubjectH5Detail;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 * 【课程统计/学情监控】获取热门数据 输出参数
 */
@Data
@Accessors(chain = true)
public class SearchStudentSubjectResponse {
    /// <summary>
    /// 用户课程列表
    /// </summary>
    private List<GetUserSubjectH5Detail> SubjectList;

    public SearchStudentSubjectResponse() {
        SubjectList = new LinkedList<>();
    }
}
