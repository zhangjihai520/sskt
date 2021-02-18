package com.ry.sskt.model.examset.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.examset.entity.GetExamSetListDetail;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class GetAddExamSetUrlResponse {


    /// <summary>
    /// 布置新题地址
    /// </summary>
    @JSONField(name = "AddExamSetUrl")
    private String addExamSetUrl;

}
