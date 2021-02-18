package com.ry.sskt.model.teacher.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.common.request.RequestBase;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;
/// <summary>
/// 【课程管理】我的课程-导入学生Request
/// </summary>
@Data
@Accessors(chain = true)
public class ImportStudentApiRequest extends RequestBase {
    /// <summary>
    /// 课程id，加密
    /// </summary>
    private String SubjectId;

    /// <summary>
    /// 教室ID，加密
    /// </summary>
    private String SubjectRoomId;


    @JSONField(name = "Files")
    private MultipartFile[] files;
}
