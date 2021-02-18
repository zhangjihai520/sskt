package com.ry.sskt.model.subject.entity;

import com.ry.sskt.model.subject.entity.view.SubjectStatisticsInfo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

/**
 * 获取课程列表班级信息详情
 */
@Data
@Accessors(chain = true)
public class GetUserSubjectListH5ByDay {
    /// <summary>
    /// 所在日期
    /// </summary>
    private String BeginDate ;

    /// <summary>
    /// 用户课程列表
    /// </summary>
    private List<GetUserSubjectH5Detail> SubjectList ;

    public GetUserSubjectListH5ByDay(){
        SubjectList = new LinkedList<>();
    }

}
