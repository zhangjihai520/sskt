package com.ry.sskt.controller.action.impl;

import com.ry.sskt.controller.action.IUserActionService;
import com.ry.sskt.core.utils.UrlUtil;
import com.ry.sskt.model.common.constant.UserTypeEnum;
import com.ry.sskt.model.common.entity.User;
import com.ry.sskt.model.common.response.ResponseBase;
import com.ry.sskt.model.user.request.SignReadMessageApiRequest;
import com.ry.sskt.model.user.response.GetUserBaseInfoResponse;
import com.ry.sskt.model.user.response.GetUserInfoResponse;
import com.ry.sskt.service.IGradeCourseService;
import com.ry.sskt.service.IMessageService;
import com.ry.sskt.service.ISubjectRoomStudentService;
import com.ry.sskt.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 * 消息 服务实现类
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
@Slf4j
@Service
public class UserActionService implements IUserActionService {
    @Autowired
    IUserService userService;

    @Autowired
    IGradeCourseService gradeCourseService;

    @Autowired
    ISubjectRoomStudentService subjectRoomStudentService;

    @Autowired
    IMessageService messageService;

    @Override
    public ResponseBase GetUserBaseInfo(User user) {
        var response = new GetUserBaseInfoResponse();
        response.setUserId(UrlUtil.encrypt(user.getUserId()));
        response.setUserName(user.getUserName());
        response.setUserTrueName(user.getUserTrueName());
        response.setUserFace(user.getUserFace());
        response.setUserRoles(user.getUserRoles());
        response.setGradeId(user.getGradeId() + "");
        return ResponseBase.GetSuccessResponse(response);
    }

    @Override
    public ResponseBase GetUserInfo(User user) {
        var gradeList = gradeCourseService.getAllGrade();
        var grade = gradeList.stream().filter(x -> x.getGradeId() == user.getGradeId()).findFirst().orElse(null);
        var response = new GetUserInfoResponse()
                .setEmail(user.getEmailAddress())
                .setUserTrueName(user.getUserTrueName())
                .setSchoolName(user.getSchoolName())
                .setGradeId(user.getGradeId())
                .setGradeName(grade == null ? StringUtils.EMPTY : grade.getGradeName())
                .setComment(user.getComment()).setUserFace(user.getUserFace()).setPhone(user.getPhone());
        if (user.getUserTypeId() == UserTypeEnum.Student.getCode()) {
            response.setSubjectCount(subjectRoomStudentService.GetSubjectCountByUserId(user.getUserId()));
            response.setExamCount(subjectRoomStudentService.GetExamSetCountByUserId(user.getUserId()));
        }
        return ResponseBase.GetSuccessResponse(response);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseBase SignReadMessage(SignReadMessageApiRequest request) throws Exception {
        var messageId = UrlUtil.decrypt(request.getMessageId(), Integer.class);
        var message = messageService.GetByKey(messageId);
        if (message == null) {
            ResponseBase.GetErrorResponse("消息不存在");
        }
        message.setStatusFlag(1);
        message.setUpdateDateTime(LocalDateTime.now());
        messageService.Save(message);
        return ResponseBase.GetSuccessResponse(1);
    }
}
