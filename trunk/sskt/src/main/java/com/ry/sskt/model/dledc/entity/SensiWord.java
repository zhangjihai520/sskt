package com.ry.sskt.model.dledc.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.core.annotation.IRedisStoredObject;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
public class SensiWord {

    @JSONField(name = "WordId")
    private int wordId;


    @JSONField(name = "CourseTypeID")
    private int courseTypeId;


    @JSONField(name = "Content")
    private String content;

    @JSONField(name = "StatusFlag")
    private int statusFlag;
}
