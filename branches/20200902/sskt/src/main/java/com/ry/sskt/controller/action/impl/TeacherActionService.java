package com.ry.sskt.controller.action.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ry.sskt.controller.action.ILoginActionService;
import com.ry.sskt.controller.action.ITeacherActionService;
import com.ry.sskt.core.config.CommonConfig;
import com.ry.sskt.core.dledc.request.CheckUserAccountRequest;
import com.ry.sskt.core.dledc.response.CheckUserAccountResponse;
import com.ry.sskt.core.utils.ExcelUtils;
import com.ry.sskt.core.utils.JwtUtil;
import com.ry.sskt.core.utils.UrlUtil;
import com.ry.sskt.model.common.constant.EvaluateTypeEnum;
import com.ry.sskt.model.common.constant.LoginTypeEnum;
import com.ry.sskt.model.common.constant.SourceTypeEnum;
import com.ry.sskt.model.common.entity.ThreeTuple;
import com.ry.sskt.model.common.entity.User;
import com.ry.sskt.model.common.entity.UserLoginInfo;
import com.ry.sskt.model.common.response.ResponseBase;
import com.ry.sskt.model.student.entity.EvaluateRecord;
import com.ry.sskt.model.student.entity.SubjectRoomStudent;
import com.ry.sskt.model.teacher.entity.TeacherInfo;
import com.ry.sskt.model.teacher.request.EvaluateStudentApiRequest;
import com.ry.sskt.model.teacher.request.ImportStudentApiRequest;
import com.ry.sskt.model.teacher.request.SearchTeacherInfoApiRequest;
import com.ry.sskt.model.teacher.response.ImportStudentResponse;
import com.ry.sskt.model.teacher.response.SearchTeacherInfoResponse;
import com.ry.sskt.model.user.request.LoginApiRequest;
import com.ry.sskt.model.user.response.LoginResponse;
import com.ry.sskt.service.*;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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
public class TeacherActionService implements ITeacherActionService {
    @Autowired
    IUserService userService;

    @Autowired
    IExamSetService examSetService;

    @Autowired
    IEvaluateRecordService evaluateRecordService;

    @Autowired
    ISubjectRoomStudentService subjectRoomStudentService;

    @Autowired
    ISubjectRoomService subjectRoomService;

    @Override
    public ResponseBase SearchTeacherInfo(SearchTeacherInfoApiRequest request) {
        var result = new SearchTeacherInfoResponse();
        var teachers = userService.getListBySearch(1, request.getTeacherName(), 1, 50);
        for (User m : teachers.second) {
            result.getTeacherList().add(new TeacherInfo().setId(UrlUtil.encrypt(m.getUserId())).setName(m.getUserTrueName()).setSchoolName(m.getSchoolName()));
        }
        return ResponseBase.GetSuccessResponse(result);
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseBase EvaluateStudent(EvaluateStudentApiRequest request) throws Exception {
        var subjectId = UrlUtil.decrypt(request.getSubjectId(), Integer.class);
        var studentId = UrlUtil.decrypt(request.getStudentId(), Integer.class);
        var evaluateRecord = evaluateRecordService.GetByKey(subjectId, request.getUserId(), studentId);
        if (evaluateRecord != null) {
            return ResponseBase.GetErrorResponse("已评价此学生");
        }
        var addEvaluateRecord = new EvaluateRecord()
                .setSubjectId(subjectId)
                .setSourseUserId(request.getUserId())
                .setTargetUserId(studentId)
                .setEvaluateTypeId(EvaluateTypeEnum.TeacherEvaluateStudent.getCode())
                .setEvaluateLevel(request.getEvaluateLevel())
                .setEvaluateComment(request.getEvaluateComment())
                .setStatusFlag(1).setCreateDateTime(LocalDateTime.now()).setUpdateDateTime(LocalDateTime.now());
        evaluateRecordService.Save(addEvaluateRecord);
        return ResponseBase.GetSuccessResponse();
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseBase ImportStudent(MultipartFile[] file, String param) {
        ImportStudentApiRequest request = StringUtils.isBlank(param) ? new ImportStudentApiRequest() : JSON.parseObject(param, new TypeReference<ImportStudentApiRequest>() {
        });
        request.setFiles(file);
        if (file.length != 1 || file[0] == null) {
            return ResponseBase.GetSuccessResponse(new ImportStudentResponse().getMessageList().add("上传文件为空"));
        }
        MultipartFile multipartFile = file[0];
        String fileExtension = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));
        fileExtension = StringUtils.isNotBlank(fileExtension) ? fileExtension.toLowerCase() : StringUtils.EMPTY;
        if (fileExtension != ".xls" && fileExtension != ".xlsx") {
            return ResponseBase.GetSuccessResponse(new ImportStudentResponse().getMessageList().add("文件格式错误"));
        }
        if (multipartFile.getSize() > 10485760) {
            return ResponseBase.GetSuccessResponse(new ImportStudentResponse().getMessageList().add("文件大小超过限制"));
        }
        Sheet sheetReader = ExcelUtils.CreateReader(multipartFile);
        if (!ValidHeader(sheetReader)) {
            return ResponseBase.GetSuccessResponse(new ImportStudentResponse().getMessageList().add("表格头部错误"));
        }
        var subjectId = UrlUtil.decrypt(request.getSubjectId(), Integer.class);
        var subjectRoomId = UrlUtil.decrypt(request.getSubjectRoomId(), Integer.class);
        var messageList = new LinkedList<String>();
        var totalCount = 0;
        var studentList = GetStudentList(sheetReader, messageList, totalCount);
        var subjectRoomStudentList = ValidateStudent(studentList, messageList);
        if (subjectRoomStudentList.size() != 0) {
            for (var item : subjectRoomStudentList) {
                item.setSubjectId(subjectId);
                item.setSubjectRoomId(subjectRoomId);
            }
            subjectRoomStudentService.BatchInsertSubjectRoomStudent(subjectRoomStudentList);
            subjectRoomService.refreshRegisterNumber(subjectRoomId);
        }
        messageList.add(0, String.format("总共%s个学生，成功%s个，失败%s个！", totalCount, subjectRoomStudentList.size(), totalCount - subjectRoomStudentList.size()));
        return ResponseBase.GetSuccessResponse(new ImportStudentResponse().setMessageList(messageList));
    }


    /// <summary>
    /// 校验表头
    /// </summary>
    /// <param name="sheetReader"> </param>
    /// <returns></returns>
    private static boolean ValidHeader(Sheet sheetReader) {
        if (sheetReader == null) {
            return false;
        }
        Row row = sheetReader.getRow(0);
        Cell headerFirst = row.getCell(0);
        Cell headerSecond = row.getCell(1);
        return ("学籍号").equals(ExcelUtils.getCellValue(headerFirst)) && ("学生名称").equals(ExcelUtils.getCellValue(headerSecond));
    }

    /// <summary>
    /// 获取文件中学生列表
    /// 行号、学籍号、学生姓名
    /// </summary>
    /// <returns></returns>
    private List<ThreeTuple<Integer, String, String>> GetStudentList(Sheet sheetReader, List<String> messageList, int totalCount) {
        totalCount = 0;
        var result = new LinkedList<ThreeTuple<Integer, String, String>>();
        for (var i = sheetReader.getFirstRowNum() + 1; i <= sheetReader.getLastRowNum(); i++) {
            Row row = sheetReader.getRow(i);
            Cell firstCell = row.getCell(0);
            Cell secondCell = row.getCell(1);
            String studentCode = ExcelUtils.getCellValue(firstCell);
            String studentName = ExcelUtils.getCellValue(secondCell);
            if (StringUtils.isBlank(studentCode) && StringUtils.isBlank(studentName)) {
                break;
            }
            totalCount++;
            if (StringUtils.isBlank(studentCode)) {
                messageList.add(String.format("第%s行 学籍号不能为空", i + 1));
                continue;
            }
            if (StringUtils.isBlank(studentName)) {
                messageList.add(String.format("第%s行 学生姓名不能为空", i + 1));
                continue;
            }
            if (result.stream().anyMatch(x -> x.second == studentCode)) {
                messageList.add(String.format("第%s行 学籍号重复", i + 1));
                continue;
            }
            result.add(new ThreeTuple<Integer, String, String>(i + 1, studentCode, studentName));
        }
        return result;
    }

    /// <summary>
    /// 校验学生列表，并返回校验成功的数据
    /// </summary>
    /// <param name="studentList"></param>
    /// <param name="messageList"></param>
    /// <returns></returns>
    private List<SubjectRoomStudent> ValidateStudent(List<ThreeTuple<Integer, String, String>> studentList, List<String> messageList) {
        var result = new LinkedList<SubjectRoomStudent>();
        for (int i = 0; i < Math.ceil(studentList.size() / 100.0); i++) {
            var tempList = studentList.stream().skip(i * 100).limit(100).collect(Collectors.toList());
            List<String> codelist = tempList.stream().map(e -> e.second).collect(Collectors.toList());
            List<User> userList = userService.getListByStudentCodes(codelist);
            for (var item : tempList) {
                var user = userList.stream().filter(x -> x.getStudentCode().equals(item.second)).findFirst().orElse(null);
                if (user != null) {
                    if (!user.getUserTrueName().equals(item.third)) {
                        messageList.add(String.format("第%s行 学籍号和学生姓名不匹配", item.first));
                        continue;
                    }
                } else {
                    messageList.add(String.format("第%s行 学籍号%s不存在", item.first, item.second));
                    continue;
                }
                result.add(new SubjectRoomStudent().setUserId(user.getUserId()).setIsAbsent(0).setIsEvaluate(0).setStatusFlag(1).setCreateDateTime(LocalDateTime.now()).setUpdateDateTime(LocalDateTime.now()));
            }
        }
        return result;
    }
}
