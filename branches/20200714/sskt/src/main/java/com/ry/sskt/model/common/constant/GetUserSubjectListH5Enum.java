package com.ry.sskt.model.common.constant;

import lombok.Getter;
import lombok.Setter;

/**
 * @Auther: 幸仁强
 * @Date: 2019/7/16 15:34
 * @Description: 用户类型
 */
public enum GetUserSubjectListH5Enum {

    /// <summary>
    /// 我的数据
    /// </summary>
    Mine(0),
    /// <summary>
    /// 全部数据
    /// </summary>
    All(1);

    @Getter
    @Setter
    private Integer Code;

    // 构造方法
    GetUserSubjectListH5Enum(int Code) {
        this.Code = Code;
    }

}
