package com.ry.sskt.model.common.constant;

import lombok.Getter;

/**
 * @Auther: 幸仁强
 * @Date: 2019/7/16 15:34
 * @Description: 常量枚举
 */
public enum ConstantEnum {

    SESSION_MANAGER("SESSION_MANAGER"),

    APP_TOKEN("APP_TOKEN"),

    PHONE_VERIFY_CODE("Code:PHONE_VERIFY_CODE"),

    ACCOUNT_PHONE_VERIFY_CODE("Code:ACCOUNT_PHONE_VERIFY_CODE"),

    HX_COMMUNITY_USER("HX_COMMUNITY_USER"),

    HX_BUILDING_USER("HX_BUILDING_USER"),

    ACTIVITY_ID("ACTIVITY_ID"),

    /**
     * 商超订单相关
      */
    STORE_ORDER_SN("store:Order:order_"),


    DM_AREA("DM_AREA"),//省
    DM_AREA_PROVINCE("DM_AREA_PROVINCE"),//省
    DM_AREA_CITY("DM_AREA_CITY"),//市
    DM_AREA_DISTRICT("DM_AREA_DISTRICT"),//县
    DM_AREA_COMMUNTITY("DM_AREA_COMMUNTITY");//小区

    @Getter
    private String Code;

    // 构造方法
    ConstantEnum(String Code) {
        this.Code = Code;
    }

}
