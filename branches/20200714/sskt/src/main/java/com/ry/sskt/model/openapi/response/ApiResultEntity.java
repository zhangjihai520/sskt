package com.ry.sskt.model.openapi.response;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <描述类的作用>
 *
 * @author 幸仁强
 * @ClassName: ApiResultEntity
 * @date 2018年12月18日
 */
@Data
@Accessors(chain = true)
public class ApiResultEntity {
    // 状态 1成功，99失败
    private int apiResultType;

    // 消息
    private String resultMessage;

    // 结果
    private String value;

}
