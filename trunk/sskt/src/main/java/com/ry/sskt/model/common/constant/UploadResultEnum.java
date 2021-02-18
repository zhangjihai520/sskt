package com.ry.sskt.model.common.constant;

import lombok.Getter;
import lombok.Setter;

/**
 * @Auther: 幸仁强
 * @Date: 2019/7/16 15:34
 * @Description: 用户类型
 */
public enum UploadResultEnum {

    /**
     * 成功
     */
    Success(1),

    /**
     * 文件类型不支持
     */
    FileExtensionNotSupport(2),

    /**
     * 文件超出最大限制
     */
    FileSizeOverFlow(3),

    /**
     * 文件上传失败
     */
    Failed(4),

    /**
     * 其他失败原因 - 99
     */
    Fail (99);

    @Getter
    @Setter
    private Integer Code;

    // 构造方法
    UploadResultEnum(int Code) {
        this.Code = Code;
    }

}
