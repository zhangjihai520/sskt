package com.ry.sskt.model.common.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

/// <summary>
/// 上传文件返回结果
/// </summary>
@Data
@Accessors(chain = true)
public class UploadFileResult {
    /// <summary>
    /// UploadResultEnum 上传结果枚举
    /// </summary>
    @JSONField(name = "Result")
    private int result;

    /// <summary>
    /// 文件路径（被处理过名称）
    /// </summary>
    @JSONField(name = "FilePath")
    private String filePath;

    /// <summary>
    /// 文件名称（存储接口需传入真实名称）
    /// </summary>
    @JSONField(name = "FileName")
    private String fileName;

    /// <summary>
    /// 文件大小（存储接口需传入真实名称）
    /// </summary>
    @JSONField(name = "FileSize")
    private long fileSize;

    public UploadFileResult() {
    }

    public UploadFileResult(int result) {
        this.result = result;
        this.fileSize = 0;
        this.filePath = StringUtils.EMPTY;
        this.fileName = StringUtils.EMPTY;

    }


    public UploadFileResult(int result, long fileSize, String filePath, String fileName) {
        this.result = result;
        this.fileSize = fileSize;
        this.filePath = filePath;
        this.fileName = fileName;
    }
}
