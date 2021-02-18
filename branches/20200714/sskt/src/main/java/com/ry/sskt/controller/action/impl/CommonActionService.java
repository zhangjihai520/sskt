package com.ry.sskt.controller.action.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.ry.sskt.controller.action.ICommonActionService;
import com.ry.sskt.core.config.CommonConfig;
import com.ry.sskt.core.utils.*;
import com.ry.sskt.model.common.constant.CommonConst;
import com.ry.sskt.model.common.constant.UploadResultEnum;
import com.ry.sskt.model.common.entity.*;
import com.ry.sskt.model.common.request.*;
import com.ry.sskt.model.common.response.CurrentTimeResponse;
import com.ry.sskt.model.common.response.GetGradeListResponse;
import com.ry.sskt.model.common.response.ResponseBase;
import com.ry.sskt.model.common.response.UploadFileResult;
import com.ry.sskt.model.openapi.entity.BookVersion;
import com.ry.sskt.model.openapi.entity.CourseMapping;
import com.ry.sskt.model.subject.entity.view.SubjectListByFilterView;
import com.ry.sskt.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.MimeMappings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 消息 服务实现类
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
@Service
public class CommonActionService implements ICommonActionService {
    @Autowired
    IOpenApiService openApiService;

    @Autowired
    IGradeCourseService gradeCourseService;

    @Autowired
    ISubjectService subjectService;

    @Autowired
    IVideoPlayService videoPlayService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseBase GetBookVersionList(GetBookVersionListApiRequest request) throws Exception {
        List<BookVersionInfo> bookVersionInfos = null;
        List<BookVersion> bookVersions = openApiService.getBookVersions(new int[]{request.getCourseId()});
        if (CollectionUtils.isNotEmpty(bookVersions)) {
            bookVersionInfos = bookVersions.stream().map(m -> {
                return new BookVersionInfo(m.getBookVersionId(), m.getBookVersionName(), m.getCourseId());
            }).collect(Collectors.toList());
        }
        return ResponseBase.GetSuccessResponse(bookVersionInfos);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseBase GetCourseMappingList(GetCourseMappingListApiRequest request) throws Exception {
        List<CourseMappingInfo> courseMappingInfos = null;
        List<CourseMapping> courseMappings = openApiService.getCourseMappings(new int[]{request.getBookVersionId()});
        if (CollectionUtils.isNotEmpty(courseMappings)) {
            courseMappingInfos = courseMappings.stream().map(m -> {
                return new CourseMappingInfo(m.getCourseMappingId(), m.getCourseMappingName(), m.getBookVersionId());
            }).collect(Collectors.toList());
        }
        return ResponseBase.GetSuccessResponse(courseMappingInfos);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseBase GetChapterSectionList(GetChapterSectionListApiRequest request) throws Exception {
        List<SimpleTreeNode> simpleTreeNodes = null;
        List<ChapterSection> chapterSections = openApiService.getChapterSections(new int[]{request.getCourseMappingId()});
        if (CollectionUtils.isNotEmpty(chapterSections)) {
            simpleTreeNodes = chapterSections.stream().map(m -> {
                return new SimpleTreeNode(m.getParentNodeId(), m.getNodeId(), m.getNodeName(), m.getOrderIndex());
            }).collect(Collectors.toList());
        }
        return ResponseBase.GetSuccessResponse(simpleTreeNodes);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseBase GetGradeList() throws Exception {
        GetGradeListResponse result = new GetGradeListResponse();
        List<Course> allCourse = gradeCourseService.getAllCourse(true);
        List<CourseType> allCourseType = gradeCourseService.getAllCourseType();
        List<Grade> allGrade = gradeCourseService.getAllGrade();
        allCourseType.sort(Comparator.comparing(CourseType::getOrderIndex));
        result.getCourseTypeList().addAll(allCourseType.stream().map(x -> {
            return new IdName(x.getCourseTypeId().toString(), x.getCourseTypeName());
        }).collect(Collectors.toList()));
        List<CourseTypeGrade> courseTypeGrades = new LinkedList<>();
        allGrade.forEach(x -> {
            allCourseType.forEach(y -> {
                if (x.getCourseTypeId() == y.getCourseTypeId()) {
                    courseTypeGrades.add(new CourseTypeGrade(y.getOrderIndex(), x.getStudyYearOffSet(), y.getCourseTypeId(), x.getGradeId(), x.getGradeName()));
                }
            });
        });
        courseTypeGrades.sort(Comparator.comparing(CourseTypeGrade::getOrderIndex).thenComparing(CourseTypeGrade::getStudyYearOffSet));
        result.getGradeList().addAll(courseTypeGrades.stream().map(x -> {
            return new GradeInfo(x.getCourseTypeId() + "", x.getGradeId() + "", x.getGradeName());
        }).collect(Collectors.toList()));
        List<CourseTypeCourse> courseTypeCourses = new LinkedList<>();
        allCourse.forEach(x -> {
            allCourseType.forEach(y -> {
                if (x.getCourseTypeId().equals(y.getCourseTypeId())) {
                    courseTypeCourses.add(new CourseTypeCourse(y.getOrderIndex(), x.getOrderIndex(), y.getCourseTypeId(), x.getCourseId(), x.getCourseName(), x.getShortCode()));
                }
            });
        });
        courseTypeCourses.sort(Comparator.comparing(CourseTypeCourse::getOrderIndex).thenComparing(CourseTypeCourse::getSubIndex));
        result.getCourseList().addAll(courseTypeCourses.stream().map(x -> {
            return new CourseInfo(x.getCourseTypeId() + "", x.getCourseId() + "", x.getCourseName(), x.getShortCode());
        }).collect(Collectors.toList()));
        return ResponseBase.GetSuccessResponse(result);
    }

    @Override
    public ResponseBase CurrentTime(CurrentTimeApiRequest request) throws Exception {
        CurrentTimeResponse result = new CurrentTimeResponse();
        LocalDateTime now = LocalDateTime.now();
        result.setCurrentDate(now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        result.setCurrentTime(now.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        if (request != null && request.getInputDate() != null && request.getBeginTime() != null && request.getInputDate().isAfter(now) && now.isAfter(request.getBeginTime())) {
            int diff = (int) ((now.toEpochSecond(ZoneOffset.of("+8")) - request.getBeginTime().toEpochSecond(ZoneOffset.of("+8"))));
            result.setSpaceOfTime(diff);
        } else {
            result.setSpaceOfTime(-1);
        }
        List<SubjectListByFilterView> allSubjects = subjectService.getAllPublicBenefitSubject(true);
        if (CollectionUtils.isNotEmpty(allSubjects)) {
            LocalDateTime maxDate = allSubjects.stream().max(Comparator.comparing(SubjectListByFilterView::getBeginTime)).get().getBeginTime();
            LocalDateTime minDate = allSubjects.stream().min(Comparator.comparing(SubjectListByFilterView::getBeginTime)).get().getBeginTime();
            result.setMaxDate(maxDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
            result.setMinDate(minDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        }
        return ResponseBase.GetSuccessResponse(result);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseBase RecordLog(RecordLogApiRequest request) throws Exception {
        switch (request.getRecordLogType()) {
            case 0:
                int subjectId = UrlUtil.decrypt(request.getObjectKey(), Integer.class);
                videoPlayService.save(new VideoPlayInfo(subjectId, StringUtils.EMPTY, new Date(), request.getUserId(), request.getObjectType()));
                break;
            default:
                break;
        }
        return ResponseBase.GetSuccessResponse();
    }

    @Override
    public ResponseBase uploadFile(MultipartFile[] file, String param) throws Exception {
        UploadFileApiRequest uploadFileApiRequest = StringUtils.isBlank(param) ? new UploadFileApiRequest() : JSON.parseObject(param, new TypeReference<UploadFileApiRequest>() {
        });
        uploadFileApiRequest.setFiles(file);
        if (file.length != 1) {
            return ResponseBase.GetSuccessResponse(new UploadFileResult(UploadResultEnum.Fail.getCode()));
        }
        MultipartFile multipartFile = file[0];
        boolean isFileExtensionSupport = IsFileExtensionSupport(multipartFile);
        if (!isFileExtensionSupport) {
            return ResponseBase.GetSuccessResponse(new UploadFileResult(UploadResultEnum.FileExtensionNotSupport.getCode()));
        }
        if (multipartFile.getSize() > CommonConst.FileMaxLength) {
            return ResponseBase.GetSuccessResponse(new UploadFileResult(UploadResultEnum.FileSizeOverFlow.getCode()));
        }
        String fileExtension = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".")).toLowerCase();
        MimeMappings mimeMappings = new MimeMappings();
        String mimeMapping = mimeMappings.get(fileExtension.substring(1));
        String ossPath = MessageFormat.format("{0}/{1}/{2}{3}", CommonConst.OOS_File_Root_Dir_Courseware, DateUtil.dateToString(new Date(), "yyyyMM/dd"), UUID.randomUUID(), fileExtension);
        //.NET上传到了阿里云OSS，JAVA上传到天翼云OOS
        TwoTuple<Boolean, String> resultTuple = UpdateFileToCtyun(multipartFile, ossPath, mimeMapping);
        if (!resultTuple.first) {
            throw new Exception(resultTuple.second);
        }
        return ResponseBase.GetSuccessResponse(new UploadFileResult(UploadResultEnum.Success.getCode(), multipartFile.getSize(), resultTuple.second, multipartFile.getOriginalFilename()));
    }


    /// <summary>
    /// 上传文件至天翼云，成功返回文件全地址，失败返回错误信息
    /// </summary>
    /// <param name="file"></param>
    /// <param name="ossFilePath"></param>
    /// <param name="mimeMapping"></param>
    /// <returns></returns>
    public TwoTuple<Boolean, String> UpdateFileToCtyun(MultipartFile file, String oosFilePath, String mimeMapping) {
        try {
            InputStream is = new ByteArrayInputStream(file.getBytes());
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentType(mimeMapping);
            meta.setContentLength(file.getSize());
            AmazonS3 client = CreateClient.getClient(CryptogramHelper.Decrypt3DES(CommonConfig.getCtyunOssAccessKey(), CommonConfig.getConfigKey()), CryptogramHelper.Decrypt3DES(CommonConfig.getCtyunOssSecretKey(), CommonConfig.getConfigKey()), CommonConfig.getCtyunOosBucketEndPoint());
            PutObjectResult result = client.putObject(CommonConfig.getCtyunOssBucketName(), oosFilePath, is, meta);
            String msg = String.format("%s/%s/%s", CommonConfig.getCtyunOssCdnHost(), CommonConfig.getCtyunOssBucketName(), oosFilePath);
            return new TwoTuple<Boolean, String>(StringUtils.isNotBlank(result.getETag()), msg);
        } catch (Exception e) {
            return new TwoTuple<Boolean, String>(false, e.getMessage());
        }
    }

    /// <summary>
    /// 判断文件是否支持
    /// </summary>
    /// <param name="file">文件</param>
    /// <param name="request">请求参数</param>
    /// <returns></returns>
    public boolean IsFileExtensionSupport(MultipartFile file) {
        List<String> allowfileTypes = Arrays.asList(CommonConfig.getAllowFileTypes().split(","));
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
        return allowfileTypes.contains(suffix);
    }

}
