package com.ry.sskt.model.common.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <描述类的作用>
 * 
 * @ClassName: RequestBase
 * @author 幸仁强
 * @date 2018年12月17日
 */
@Data
@Accessors(chain = true)
public class RequestBase
{
    /**
     * 用户Id
     */
    @JSONField(name="UserId")
    private int userId;

    /**
     * 用户真实姓名
     */
    @JSONField(name="CurrentUserTrueName")
    private String currentUserTrueName;

    /**
     * 用户头像
     */
    @JSONField(name="UserFace")
    private String userFace;

    /**
     * 用户类型
     */
    @JSONField(name="UserTypeId")
    private int userTypeId;

    /**
     * 系统类型
     */
    @JSONField(name="SystemTypeId")
    private int systemTypeId;

    /**
     * 来源 见FromTypeEnum
     */
    @JSONField(name="FromTypeId")
    private int fromTypeId;

}
