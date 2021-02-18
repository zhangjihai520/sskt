package com.ry.sskt.model.common.constant;

import lombok.Getter;
import lombok.Setter;

/**
 * @Auther: 幸仁强
 * @Date: 2019/7/16 15:34
 * @Description: 用户类型
 */
public enum PublicBenefitUrlTypeEnum {

    /// <summary>
    /// 99公益课普通学科
    /// </summary>
    OpenUrl(99),

    /// <summary>
    /// 98公益课模拟练习
    /// </summary>
    OtherPageRedirect(98),

    /// <summary>
    /// 97公益课特殊课程跳转赣教云网址
    /// </summary>
    NewPageRedirect(97);

    @Getter
    @Setter
    private Integer Code;

    // 构造方法
    PublicBenefitUrlTypeEnum(int Code) {
        this.Code = Code;
    }

    }
