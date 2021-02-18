package com.ry.sskt.model.common.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

@Data
@Accessors(chain = true)
public class UploadFileApiRequest extends RequestBase {
    /// <summary>
    /// 1、课件文件
    /// </summary>
    @JSONField(name = "FileScene")
    private Integer FileScene;

    @JSONField(name = "Files")
    private MultipartFile[] files;
}
