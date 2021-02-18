package com.ry.sskt.controller.action;

import com.ry.sskt.model.common.response.ResponseBase;
import com.ry.sskt.model.user.request.LoginApiRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 消息 服务类
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
public interface IRtmpNotifyActionService {
    //推流权限校验
    boolean pushAuth(Map<String, String> map) throws Exception;
}
