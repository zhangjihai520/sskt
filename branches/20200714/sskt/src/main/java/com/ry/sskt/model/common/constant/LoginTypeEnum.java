package com.ry.sskt.model.common.constant;

import lombok.Getter;
import lombok.Setter;

/**
 * @Auther: 幸仁强
 * @Date: 2019/7/16 15:34
 * @Description: 登录类型
 */
public enum LoginTypeEnum {

    /**
     * 普通登录
     */
    Web(0),

    /**
     * 新华云web跳转
     */
    XinHua(1),

    /**
     * H5手机登录
     */
    H5(2),

    /**
     * 新华云web跳转
     */
    XinHuaMobile(3);

    @Getter
    @Setter
    private int Code;

    // 构造方法
    LoginTypeEnum(int Code) {
        this.Code = Code;
    }

}
