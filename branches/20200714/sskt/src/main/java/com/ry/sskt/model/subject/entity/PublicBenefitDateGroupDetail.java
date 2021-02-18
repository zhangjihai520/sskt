package com.ry.sskt.model.subject.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

@Data
@Accessors(chain = true)
public class PublicBenefitDateGroupDetail {
    private String Date;

    private List<GetPublicBenefitSubjectListDetail> GroupClassInfoList;

    public PublicBenefitDateGroupDetail() {
        GroupClassInfoList = new LinkedList<>();
    }
}
