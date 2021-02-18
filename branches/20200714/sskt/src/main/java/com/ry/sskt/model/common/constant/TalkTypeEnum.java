package com.ry.sskt.model.common.constant;

import lombok.Getter;
import lombok.Setter;

/**
 * @Auther: 幸仁强
 * @Date: 2019/7/16 15:34
 * @Description: 发言类型
 */
public enum TalkTypeEnum {

    /**
     * 纯文本
     */
    Text(1),

    /**
     * 图片
     */
    Image(2),

    /**
     * 附件
     */
    Attachment(3);

    @Getter
    @Setter
    private int Code;

    // 构造方法
    TalkTypeEnum(int Code) {
        this.Code = Code;
    }

}
