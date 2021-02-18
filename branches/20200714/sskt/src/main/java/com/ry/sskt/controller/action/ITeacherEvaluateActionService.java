package com.ry.sskt.controller.action;

import com.ry.sskt.model.common.request.RequestPageBase;
import com.ry.sskt.model.common.response.ResponseBase;
import com.ry.sskt.model.teacher.request.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 消息 服务类
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
public interface ITeacherEvaluateActionService {
    ResponseBase EvaluateSubject(EvaluateSubjectApiRequest request) throws Exception;

    ResponseBase GetSubjectEvaluateList(RequestPageBase request);

    ResponseBase ReplayMessage(ReplayMessageApiRequest request) throws Exception;

    ResponseBase GetMessageList(GetMessageListApiRequest request);

    ResponseBase GetAllSubjectEvaluate(RequestPageBase request);

    ResponseBase GetSubjectEvaluate(GetSubjectEvaluateApiRequest request);

}
