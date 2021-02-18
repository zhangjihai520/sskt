package com.ry.sskt.model.subject.entity.cache;

import com.ry.sskt.core.annotation.IRedisStoredObject;
import com.ry.sskt.model.common.entity.Course;
import com.ry.sskt.model.subject.entity.view.SubjectListByFilterView;
import io.protostuff.Tag;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class AllPublicBenefitSubjectListCouchBaseObject implements Serializable, IRedisStoredObject {


    private List<SubjectListByFilterView> allPublicBenefitSubject;

    private static String key = "AllPublicBenefitSubjects";
    @Override
    public String getKey() {
        return key;
    }
}
