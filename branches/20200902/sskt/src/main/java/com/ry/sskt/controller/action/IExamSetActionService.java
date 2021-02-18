package com.ry.sskt.controller.action;

import com.ry.sskt.model.common.response.ResponseBase;
import com.ry.sskt.model.dledc.request.DledcConfigApiRequest;
import com.ry.sskt.model.dledc.request.DledcLoginApiRequest;
import com.ry.sskt.model.dledc.request.DledcMobileLoginApiRequest;
import com.ry.sskt.model.examset.request.*;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 * 消息 服务类
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
public interface IExamSetActionService {
    ResponseBase GetExamSetList(GetExamSetListApiRequest request);

    ResponseBase GetExamSetStatistList(GetExamSetStatistListApiRequest request);

    ResponseBase GetAddExamSetUrl(GetAddExamSetUrlApiRequest request) throws Exception;

    ResponseBase GetEvaluationResult(GetEvaluationResultApiRequest request) throws Exception;

    ResponseBase SetExamSetOnline(SetExamSetOnlineApiRequest request);

    ResponseBase GetExamSetPreview(GetExamSetPreviewApiRequest request) throws Exception;

    ResponseBase SaveExamSet(SaveExamSetApiRequest request);

    ResponseBase GetExamSetInfo(GetExamSetInfoApiRequest request) throws Exception;


}
