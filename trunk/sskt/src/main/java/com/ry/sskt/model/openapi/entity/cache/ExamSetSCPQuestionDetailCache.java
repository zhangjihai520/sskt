package com.ry.sskt.model.openapi.entity.cache;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ry.sskt.core.annotation.IRedisStoredObject;
import com.ry.sskt.model.examset.entity.SCPQuestionDetail;
import com.ry.sskt.model.examset.view.SCPQuestionDetailView;
import com.ry.sskt.model.openapi.entity.CourseMapping;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class ExamSetSCPQuestionDetailCache implements Serializable, IRedisStoredObject {

    private Integer examSetId;

    private SCPQuestionDetailView scpQuestionDetailView;

    private static String key = "ExamSetSCPQuestionDetail:ExamSetId_%s";

    public ExamSetSCPQuestionDetailCache() {
    }

    public ExamSetSCPQuestionDetailCache(Integer examSetId) {
        this.examSetId = examSetId;
    }

    public ExamSetSCPQuestionDetailCache(Integer examSetId, SCPQuestionDetailView scpQuestionDetailView) {
        this.examSetId = examSetId;
        this.scpQuestionDetailView = scpQuestionDetailView;
    }

    @Override
    public String getKey() {
        return StringUtils.format(key, examSetId);
    }
}
