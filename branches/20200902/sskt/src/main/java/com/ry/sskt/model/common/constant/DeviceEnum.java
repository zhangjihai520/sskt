package com.ry.sskt.model.common.constant;

import lombok.Getter;
import lombok.Setter;

/**
 * @Auther: 幸仁强
 * @Date: 2019/7/16 15:34
 * @Description: 用户类型
 */
public enum DeviceEnum {

    /**
     * 离线
     */
    offline(1, "offline"),

    /**
     * 在线
     */
    online(2, "online"),

    /**
     * 在会
     */
    meeting(3, "meeting");

    @Getter
    private Integer index;

    @Getter
    private String value;

    private DeviceEnum(int index, String value) {
        this.index = index;
        this.value = value;
    }

}
