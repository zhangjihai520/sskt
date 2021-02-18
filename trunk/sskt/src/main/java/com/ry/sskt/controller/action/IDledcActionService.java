package com.ry.sskt.controller.action;

import com.ry.sskt.model.common.response.ResponseBase;
import com.ry.sskt.model.discuss.request.MessageApiRequest;
import com.ry.sskt.model.discuss.request.SendFileApiRequest;
import com.ry.sskt.model.discuss.request.SendMessageApiRequest;
import com.ry.sskt.model.dledc.request.DledcConfigApiRequest;
import com.ry.sskt.model.dledc.request.DledcLoginApiRequest;
import com.ry.sskt.model.dledc.request.DledcMobileLoginApiRequest;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 * 消息 服务类
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
public interface IDledcActionService {
    ResponseBase DledcLogin(DledcLoginApiRequest request) throws Exception;

    ResponseBase DledcMobileLogin(DledcMobileLoginApiRequest request) throws Exception;

    ResponseBase DledcConfig(DledcConfigApiRequest request);
}
