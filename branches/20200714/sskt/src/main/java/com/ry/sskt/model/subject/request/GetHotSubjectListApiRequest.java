package com.ry.sskt.model.subject.request;

import com.ry.sskt.model.common.request.RequestBase;
import com.ry.sskt.model.common.request.RequestPageBase;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 *【课表管理】下架/上架/删除课程 输入参数
 */
@Data
@Accessors(chain = true)
public class GetHotSubjectListApiRequest extends RequestPageBase {
    /// <summary>
    /// 数据类型 1.热门课程 2..热门主讲教师 3 热门学校
    /// </summary>
    private int TypeEnum;

    /// <summary>
    /// 数据类型 1学生，2老师，3管理员，4家长
    /// </summary>
    private int UserRole;
}
