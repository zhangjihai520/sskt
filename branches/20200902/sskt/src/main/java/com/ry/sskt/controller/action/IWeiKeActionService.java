package com.ry.sskt.controller.action;

import com.ry.sskt.model.common.response.ResponseBase;
import com.ry.sskt.model.user.request.LoginApiRequest;
import com.ry.sskt.model.weike.request.GetWeiKeInfoRequest;
import com.ry.sskt.model.weike.request.GetWeiKeListRequest;
import com.ry.sskt.model.weike.request.SetWeiKeInfoRequest;
import com.ry.sskt.model.weike.request.UpdateWeiKeStatusRequest;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 * 消息 服务类
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
public interface IWeiKeActionService {
    ResponseBase GetWeiKeInfo(GetWeiKeInfoRequest request) throws Exception;

    ResponseBase GetWeiKeList(GetWeiKeListRequest request) throws Exception;

    ResponseBase SetWeiKeInfo(SetWeiKeInfoRequest request) throws Exception;

    ResponseBase UpdateWeiKeStatus(UpdateWeiKeStatusRequest request);

    ResponseBase GetWeiKeVideo(GetWeiKeInfoRequest request);
}
