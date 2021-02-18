package com.ry.sskt.controller.action;

import com.ry.sskt.model.common.response.ResponseBase;
import com.ry.sskt.model.dledc.request.DledcConfigApiRequest;
import com.ry.sskt.model.dledc.request.DledcLoginApiRequest;
import com.ry.sskt.model.dledc.request.DledcMobileLoginApiRequest;
import com.ry.sskt.model.user.request.LoginApiRequest;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 * 消息 服务类
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
public interface ILoginActionService {
    ResponseBase LoginWithPassword(LoginApiRequest request) throws Exception;
}
