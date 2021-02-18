package com.ry.sskt.model.subject.response;

import com.ry.sskt.model.subject.entity.GetUserSubjectListH5ByDay;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

/**
 * 获取时间段内的学生开课情况 输出参数
 */
@Data
@Accessors(chain = true)
public class GetUserSubjectListH5Response {

    /// <summary>
    /// 数据组
    /// </summary>
    private List<GetUserSubjectListH5ByDay> DateList;

    public GetUserSubjectListH5Response() {
        DateList = new LinkedList<>();
    }

}
