package com.ry.sskt.model.common.constant;

import lombok.Getter;

/**
 * 菜单类型
 */
public enum MenuTypeEnum {
    /**
     * 目录
     */
    CATALOG(0),
    /**
     * 菜单
     */
    MENU(1),
    /**
     * 按钮
     */
    BUTTON(2);

    @Getter
    private int Code;

    MenuTypeEnum(Integer Code) {
        this.Code = Code;
    }
}
