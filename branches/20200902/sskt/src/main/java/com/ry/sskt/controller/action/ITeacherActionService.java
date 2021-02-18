package com.ry.sskt.controller.action;

import com.ry.sskt.model.common.request.RequestBase;
import com.ry.sskt.model.common.request.RequestPageBase;
import com.ry.sskt.model.common.response.ResponseBase;
import com.ry.sskt.model.student.request.*;
import com.ry.sskt.model.teacher.request.EvaluateStudentApiRequest;
import com.ry.sskt.model.teacher.request.SearchTeacherInfoApiRequest;
import io.swagger.annotations.ApiParam;
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
public interface ITeacherActionService {
    ResponseBase SearchTeacherInfo(SearchTeacherInfoApiRequest request);

    ResponseBase EvaluateStudent(EvaluateStudentApiRequest request) throws Exception;

    ResponseBase ImportStudent(MultipartFile[] file, String param);
}
