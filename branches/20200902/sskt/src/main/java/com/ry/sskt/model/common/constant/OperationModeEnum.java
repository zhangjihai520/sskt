package com.ry.sskt.model.common.constant;

import lombok.Getter;
import lombok.Setter;

/**
 * @Auther: 幸仁强
 * @Date: 2019/7/16 15:34
 * @Description: 发言类型
 */
public enum OperationModeEnum {

    /**
     * 直接播放
     */
    Play(0),

    /**
     * 模拟练习
     */
    SimulationExercise(1),
    /**
     * 跳转网页
     */
    PopupPage(2),

    /**
     * 无录像
     */
    NoUrl(3);

    @Getter
    @Setter
    private Integer Code;

    // 构造方法
    OperationModeEnum(int Code) {
        this.Code = Code;
    }

}
