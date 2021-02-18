package com.ry.sskt.model.common.constant;

import lombok.Getter;
/**
 * @Auther: 幸仁强
 * @Date: 2019/7/16 15:34
 * @Description: 字典Key枚举
 */
public enum DictionaryKeyEnum {
    /**
     * 服务类型
     */
    SERVICE_TYPE("serviceType"),
    /**
     * 服务标签
     */
    SERVICE_TAG("serviceTag"),
    /**
     * 帖子所属板块
     */
    NOTE_TYPE("noteType"),

    /**
     * 银行类型
     */
   BANK_TYPE("bankType"),

    /**
     * 银行类型
     */
    KW_TELEPHONE("kwTelephoneNumber"),

    /**
     * 举报发帖类型
     */
    REPORT_TYPE("reportType"),
    /**
     * ios下载里链接
     */
    IOSAPP_TYPE("iosAppType"),

    /**
     * 房屋类型
     */
    HOUSE_TYPE("houseType"),

    /**
     * 员工评价标签
     */
    COMMENT_TAG("commentTag");

    @Getter
    private String key;

    // 构造方法
    DictionaryKeyEnum(String Key) {
        this.key = Key;
    }
}
