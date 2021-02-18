package com.ry.sskt.controller.action;

import com.ry.sskt.model.common.entity.User;
import com.ry.sskt.model.common.request.RequestBase;
import com.ry.sskt.model.common.response.ResponseBase;
import com.ry.sskt.model.user.request.LoginApiRequest;
import com.ry.sskt.model.user.request.SignReadMessageApiRequest;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 * 消息 服务类
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
public interface IUserActionService {
    ResponseBase GetUserBaseInfo(User user);

    ResponseBase GetUserInfo(User user);

    ResponseBase SignReadMessage(SignReadMessageApiRequest request) throws Exception;
}
