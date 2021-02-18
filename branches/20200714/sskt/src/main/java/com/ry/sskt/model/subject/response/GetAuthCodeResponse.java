package com.ry.sskt.model.subject.response;

import com.ry.sskt.model.video.entity.view.DeviceView;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 获取授权码
 */
@Data
@Accessors(chain = true)
public class GetAuthCodeResponse {
    private String AuthCode;
}
