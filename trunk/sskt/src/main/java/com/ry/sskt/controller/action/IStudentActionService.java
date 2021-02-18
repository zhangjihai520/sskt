package com.ry.sskt.controller.action;

import com.ry.sskt.model.common.request.RequestBase;
import com.ry.sskt.model.common.request.RequestPageBase;
import com.ry.sskt.model.common.response.ResponseBase;
import com.ry.sskt.model.student.request.*;
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
public interface IStudentActionService {
    ResponseBase EditStudentInfo(EditStudentInfoApiRequest request) throws Exception;

    ResponseBase SubmitExamSet(SubmitExamSetApiRequest request) throws Exception;

    ResponseBase AskQuestion(AskQuestionApiRequest request) throws Exception;

    ResponseBase EvaluateSubject(EvaluateSubjectApiRequest request) throws Exception;

    ResponseBase GetStudentStudyRecords(GetStudentStudyRecordsApiRequest request);

    ResponseBase GetSubjectEvaluateList(RequestPageBase request);

    ResponseBase GetTeacherToStudentEvaluateList(RequestPageBase request);

    ResponseBase GetUnReadMessageNumber(RequestBase request);
}
