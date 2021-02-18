package com.ry.sskt.model.common.constant;

import lombok.Getter;
import lombok.Setter;

/**
 * @Auther: 幸仁强
 * @Date: 2019/7/16 15:34
 * @Description: 来源类型枚举
 */
public enum SourceTypeEnum {

    /**
     * 1教辅平台
     */
    JiaoFuPingTai(1),
    ///// <summary>
    ///// 2导入学生
    ///// </summary>
    //Import = 2,
    /**
     * 3注册
     */
    Register (3);

    @Getter
    @Setter
    private Integer Code;

    // 构造方法
    SourceTypeEnum(int Code) {
        this.Code = Code;
    }

}
