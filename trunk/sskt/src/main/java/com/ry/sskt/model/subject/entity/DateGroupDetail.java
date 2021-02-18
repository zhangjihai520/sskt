package com.ry.sskt.model.subject.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Data
@Accessors(chain = true)
public class DateGroupDetail {
    private String Date;

    private List<GetSubjectListDetail> GroupClassInfoList;

    public DateGroupDetail()
    {
        GroupClassInfoList = new LinkedList<>();
    }
}
