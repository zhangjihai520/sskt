package com.ry.sskt.controller;


import com.alibaba.fastjson.JSON;
import com.ry.sskt.controller.action.ITeacherActionService;
import com.ry.sskt.core.annotation.AllowUserType;
import com.ry.sskt.model.common.constant.UserTypeEnum;
import com.ry.sskt.model.common.response.ResponseBase;
import com.ry.sskt.model.teacher.request.EvaluateStudentApiRequest;
import com.ry.sskt.model.teacher.request.SearchTeacherInfoApiRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/Teacher")
@AllowUserType(userType = UserTypeEnum.All)
@Api(value = "教师相关接口", tags = {"教师相关接口"})
@Slf4j
public class TeacherController {
    @Autowired
    ITeacherActionService teacherActionService;


    @ApiOperation("模糊搜索所有教师名称")
    @ApiImplicitParam(name = "request", dataType = "SearchTeacherInfoApiRequest", paramType = "body", required = true, value = "模糊搜索所有教师名称入参实体")
    @PostMapping("/SearchTeacherInfo")
    public ResponseBase SearchTeacherInfo(@RequestBody SearchTeacherInfoApiRequest request) {
        try {
            return teacherActionService.SearchTeacherInfo(request);
        } catch (Exception ex) {
            log.error(String.format("模糊搜索所有教师名称报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("【课程详情】保存对学生评价")
    @ApiImplicitParam(name = "request", dataType = "EvaluateStudentApiRequest", paramType = "body", required = true, value = "【课程详情】保存对学生评价入参实体")
    @PostMapping("/EvaluateStudent")
    public ResponseBase EvaluateStudent(@RequestBody EvaluateStudentApiRequest request) {
        try {
            return teacherActionService.EvaluateStudent(request);
        } catch (Exception ex) {
            log.error(String.format("【课程详情】保存对学生评价报错，参数：%s", JSON.toJSONString(request)), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

    @ApiOperation("【课程管理】我的课程-导入学生")
    @PostMapping("/ImportStudent")
    public ResponseBase ImportStudent(@ApiParam(name = "file", required = true) MultipartFile[] file, @ApiParam(name = "param", required = false) String param) {
        try {
            return teacherActionService.ImportStudent(file, param);
        } catch (Exception ex) {
            log.error(String.format("【课程管理】我的课程-导入学生报错"), ex);
            return ResponseBase.GetErrorResponse();
        }
    }

}
