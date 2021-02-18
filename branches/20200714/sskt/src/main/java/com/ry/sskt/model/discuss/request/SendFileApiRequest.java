package com.ry.sskt.model.discuss.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.common.request.RequestBase;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

@Data
@Accessors(chain = true)
public class SendFileApiRequest {
    /// <summary>
    /// 课程Id
    /// </summary>
    @JSONField(name = "SubjectId")
    private String subjectId;
    /// <summary>
    /// 文件类型
    /// </summary>
    @JSONField(name = "FileType")
    private int fileType;

    @JSONField(name = "file")
    private MultipartFile file;

    /**
     * 用户Id
     */
    @JSONField(name = "UserId")
    private int userId;

    /**
     * 用户类型
     */
    @JSONField(name = "UserTypeId")
    private int userTypeId;
    /**
     * 用户真实姓名
     */
    @JSONField(name="CurrentUserTrueName")
    private String currentUserTrueName;

    /**
     * 用户头像
     */
    @JSONField(name="UserFace")
    private String userFace;
}
