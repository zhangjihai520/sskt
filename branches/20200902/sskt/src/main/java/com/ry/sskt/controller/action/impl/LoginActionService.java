package com.ry.sskt.controller.action.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.ry.sskt.controller.action.IExamSetActionService;
import com.ry.sskt.controller.action.ILoginActionService;
import com.ry.sskt.core.config.CommonConfig;
import com.ry.sskt.core.dledc.request.CheckUserAccountRequest;
import com.ry.sskt.core.dledc.response.CheckUserAccountResponse;
import com.ry.sskt.core.utils.JwtUtil;
import com.ry.sskt.core.utils.RedisUtil;
import com.ry.sskt.core.utils.UrlUtil;
import com.ry.sskt.model.common.constant.*;
import com.ry.sskt.model.common.entity.*;
import com.ry.sskt.model.common.response.ResponseBase;
import com.ry.sskt.model.examset.entity.*;
import com.ry.sskt.model.examset.request.*;
import com.ry.sskt.model.examset.response.*;
import com.ry.sskt.model.examset.view.ExamSetListView;
import com.ry.sskt.model.examset.view.GetStatistListView;
import com.ry.sskt.model.examset.view.SCPEvaluationResultView;
import com.ry.sskt.model.examset.view.SCPQuestionDetailView;
import com.ry.sskt.model.openapi.entity.StudentQuestion;
import com.ry.sskt.model.openapi.entity.TeacherExamDetailModel;
import com.ry.sskt.model.openapi.entity.TeacherExamQuestionModel;
import com.ry.sskt.model.openapi.request.GetUserSessionRequest;
import com.ry.sskt.model.openapi.response.ApiResultEntity;
import com.ry.sskt.model.openapi.response.UserBindResponse;
import com.ry.sskt.model.subject.entity.view.SubjectRoomTeacherNameView;
import com.ry.sskt.model.user.cache.TokenCache;
import com.ry.sskt.model.user.request.LoginApiRequest;
import com.ry.sskt.model.user.response.LoginResponse;
import com.ry.sskt.service.*;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
public class LoginActionService implements ILoginActionService {
    @Autowired
    IUserService userService;

    @Autowired
    IDledcService dledcService;

    @Autowired
    IUserLoginInfoService userLoginInfoService;

    //todo
    @Override
    public ResponseBase LoginWithPassword(LoginApiRequest request) throws Exception {
        var client = dledcService.getDledcConfig(1);
        var checkUserAccountRequest = new CheckUserAccountRequest(request.getUserName(), request.getPassword());
        var checkUserAccountResponse = client.Execute(CheckUserAccountResponse.class, checkUserAccountRequest);
        if (checkUserAccountResponse == null
                || checkUserAccountResponse.getStatusCode() != 1
                || checkUserAccountResponse.getData() == null
                || checkUserAccountResponse.getData().getSingleData() == null) {
            return TestGenerateSuccessResponse(Integer.parseInt(request.getUserName()));
            /**if (TestLogin()) {
                return TestGenerateSuccessResponse(Integer.parseInt(request.getUserName()));
            }
            return ResponseBase.GetSuccessResponse(new LoginResponse().setLoginStaus(0));**/
        }
        var uId = checkUserAccountResponse.getData().getSingleData().getUid();
        var userId = 0;
        User user;
        if (CommonConfig.isUseSynchronizationUse()) {
            TwoTuple<Integer, Integer> twoTuple = dledcService.sychroUser(uId);
            userId = twoTuple.first;
        } else {
            user = userService.readBySource(SourceTypeEnum.JiaoFuPingTai.getCode(), uId);
            if (user == null) {
                user = new User();
            }
            if (user.getUserId() == 0) {
                TwoTuple<Integer, Integer> twoTuple = dledcService.sychroUser(uId);
                userId = twoTuple.first;
            } else {
                userId = user.getUserId();
                if (user.getStudentCode() != "等待同步") {
                    user.setStudentCode("等待同步");
                    userService.save(user);
                }
            }
        }
        userLoginInfoService.save(new UserLoginInfo().setUserId(userId).setLoginDate(LocalDate.now()).setLoginTime(LocalDateTime.now()).setLoginType(LoginTypeEnum.Web.getCode()));
        user = userService.getByKey(userId, true);
        return generateSuccessResponse(user, request.getFromTypeId());
    }


    /// <summary>
    /// 生成返回结果
    /// </summary>
    /// <param name="user"></param>
    /// <returns></returns>
    private ResponseBase generateSuccessResponse(User user, Integer fromTypeId) {
        String token = JwtUtil.CreateToken(user.getUserId() + "", user.getUserTypeId());
        TokenCache tokenCache = new TokenCache().setToken(token).setFromTypeId(fromTypeId).setUserId(user.getUserId());
        if (fromTypeId == FromTypeEnum.PC.getIndex()) {
            RedisUtil.setObj(tokenCache, CommonConst.MINUTE_480);
        }
        if (fromTypeId == FromTypeEnum.H5.getIndex()) {
            RedisUtil.setObj(tokenCache, CommonConst.HALF_YEAR);
        }
        LoginResponse response = new LoginResponse();
        response.setLoginStaus(1);
        response.setToken(token);
        response.setUserName(user.getUserName());
        response.setUId(user.getSourceId());
        return ResponseBase.GetSuccessResponse(response);
    }

    /// <summary>
    /// 测试环境登录
    /// </summary>
    /// <returns></returns>
    private boolean TestLogin() {
        String filePath = System.getProperty("user.dir") + "\\TestLoginX13dSe215s125.txt";
        File file = new File(filePath);
        return file.exists();
    }

    /// <summary>
    /// 测试生成返回结果
    /// </summary>
    /// <param name="user"></param>
    /// <returns></returns>
    private ResponseBase TestGenerateSuccessResponse(int userId) {
        var user = userService.getByKey(userId, true);
        if (user == null || user.getUserId() == 0) {
            return ResponseBase.GetSuccessResponse(new LoginResponse().setLoginStaus(0));
        }
        var token = JwtUtil.CreateToken(user.getUserId() + "", UserTypeEnum.All.getCode());
        var response = new LoginResponse();
        response.setLoginStaus(1);
        response.setToken(token);
        response.setUserName(user.getUserName());
        response.setUId(user.getSourceId());
        return ResponseBase.GetSuccessResponse(response);
    }
}
