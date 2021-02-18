package com.ry.sskt.model.video.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.ry.sskt.model.common.constant.CommonConst;
import com.ry.sskt.model.common.constant.PublicBenefitUrlTypeEnum;
import com.ry.sskt.model.openapi.response.ApiResultEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.EntityUtils;

import java.util.LinkedList;
import java.util.List;

import static com.ry.sskt.model.common.constant.CommonConst.*;

/**
 * 硬件提供的视频地址
 */
@Data
@Accessors(chain = true)
public class VHVideoView {
    /// <summary>
    ///   0表示主讲通道，1表示ppt通道 ,公益课枚举<see cref="PublicBenefitUrlTypeEnum"/>
    /// </summary>
    private int videostype;

    /// <summary>
    /// 文件名称
    /// </summary>
    private String filename;

    /// <summary>
    /// 文件地址
    /// </summary>
    private String url;

    public VHVideoView() {
        this.filename = "";
        this.url = "";
    }


    /// <summary>
    /// 获取实例
    /// </summary>
    /// <param name="path"></param>
    /// <param name="type"></param>
    /// <param name="name"></param>
    /// <returns></returns>
    public VHVideoView(String path, int type, String name) {
        this.url = path;
        this.videostype = type;
        this.filename = name;
    }


    public static String GetPublicBenefitInstanceByJson(String url, String courseName, String fileName) {
        if (StringUtils.isBlank(fileName)) {
            fileName = "1";
        }
        var result = new VHVideoView(url, PublicBenefitUrlTypeEnum.OpenUrl.getCode(), fileName);
        if (PublicBenefitCourseNameAndType.containsKey(courseName)) {
            result.videostype = PublicBenefitCourseNameAndType.get(courseName).getCode();
        }
        var list = new LinkedList<VHVideoView>();
        list.add(result);
        return JSONArray.toJSONString(list);
    }

    public static String GetPublicBenefitUrl(String pptFilePath) {
        var result = "";
        if (StringUtils.isNotBlank(pptFilePath)) {
            var list = JSONObject.parseArray(pptFilePath, VHVideoView.class);
            result = list.stream().findFirst().orElse(new VHVideoView()).getUrl();
        }
        return result;
    }
}
