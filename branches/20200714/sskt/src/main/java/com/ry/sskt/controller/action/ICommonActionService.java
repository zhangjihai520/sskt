package com.ry.sskt.controller.action;

import com.ry.sskt.model.common.entity.TwoTuple;
import com.ry.sskt.model.common.request.*;
import com.ry.sskt.model.common.response.ResponseBase;
import com.ry.sskt.model.student.entity.Message;
import com.ry.sskt.model.student.entity.UserMessageInfo;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 消息 服务类
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
public interface ICommonActionService {
    ResponseBase GetBookVersionList(GetBookVersionListApiRequest request) throws Exception;

    ResponseBase GetCourseMappingList(GetCourseMappingListApiRequest request) throws Exception;

    ResponseBase GetChapterSectionList(GetChapterSectionListApiRequest request) throws Exception;

    ResponseBase GetGradeList() throws Exception;

    ResponseBase CurrentTime(CurrentTimeApiRequest request) throws Exception;

    ResponseBase RecordLog(RecordLogApiRequest request) throws Exception;

    ResponseBase uploadFile(MultipartFile[] file, String param) throws Exception;
}
