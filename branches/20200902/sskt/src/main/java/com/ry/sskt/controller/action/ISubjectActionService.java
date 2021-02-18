package com.ry.sskt.controller.action;

import com.ry.sskt.model.common.request.RequestBase;
import com.ry.sskt.model.common.response.ResponseBase;
import com.ry.sskt.model.subject.request.*;
import com.ry.sskt.model.user.request.LoginApiRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;
import java.util.Map;

/**
 * <p>
 * 消息 服务类
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
public interface ISubjectActionService {
    ResponseBase GetSubjectList(GetSubjectListApiRequest request);

    ResponseBase UpdateSubjectStatus(UpdateSubjectStatusApiRequest request);

    void ExportSubjectList(HttpServletRequest httprequest, HttpServletResponse response, Map<String, String> params) throws Exception;

    ResponseBase EditSubject(EditSubjectApiRequest request) throws Exception;

    ResponseBase GetSubjectInfo(GetSubjectInfoApiRequest request);

    ResponseBase GetSubjectListStatistics(GetSubjectListStatisticsApiRequest request);

    ResponseBase GetHotSubjectList(GetHotSubjectListApiRequest request);

    ResponseBase GetStudentSchoolShare(GetStudentSchoolShareApiRequest request);

    ResponseBase GetUserSubjectListH5(GetUserSubjectListH5ApiRequest request);

    ResponseBase GetStudentSubjectListH5(GetStudentSubjectListH5ApiRequest request);

    ResponseBase GetStudentSubjectList(GetStudentSubjectListApiRequest request);

    ResponseBase GetSubjectListCount(CurrentUserApiRequest request);

    ResponseBase GetTeacherSubjectDataH5(GetTeacherSubjectDataH5ApiRequest request);

    ResponseBase SearchStudentSubject(SearchStudentSubjectApiRequest request);

    ResponseBase GetSubjectInfoForShow(GetSubjectInfoForShowApiRequest request);

    ResponseBase DeleteExamSetOrFile(DeleteExamSetOrFileApiRequest request);

    ResponseBase GetStudentList(GetStudentListApiRequest request);

    ResponseBase GetSubjectFileList(GetSubjectFileListApiRequest request);

    ResponseBase SaveSubjectFile(SaveSubjectFileApiRequest request) throws Exception;

    ResponseBase RegistSubject(RegistSubjectApiRequest request);

    ResponseBase StudentGetSubjectFileList(StudentGetSubjectFileListApiRequest request);

    ResponseBase GetCurrentSubjectName(CurrentUserApiRequest request);

    ResponseBase GetSubjectVideo(GetSubjectVideoApiRequest request);

    ResponseBase UpdateSubjectStudentStatus(UpdateSubjectStudentStatusApiRequest request);

    ResponseBase UpdateSubjectStatusOfAny(UpdateSubjectStatusOfAnyApiRequest request);

    ResponseBase GetDeviceList(RequestBase request) throws Exception;

    ResponseBase CreateVideoLive(CreateVideoLiveApiRequest request) throws Exception;

    ResponseBase GetGroupByStudentList(GetGroupByStudentListRequest request);

    ResponseBase GetSubjectRoomList(GetSubjectRoomListRequest request);

    ResponseBase SaveGroupByStudentList(SaveGroupByStudentListRequest request) throws Exception;

    ResponseBase SearchSubject(SearchSubjectRequest request);

    ResponseBase GetPublicBenefitSubjectList(GetPublicBenefitSubjectListApiRequest request);

    ResponseBase GetPublicBenefitStatistics(GetPublicBenefitStatisticsApiRequest request);

    ResponseBase GetAuthCode(GetAuthCodeApiRequest request);
}
