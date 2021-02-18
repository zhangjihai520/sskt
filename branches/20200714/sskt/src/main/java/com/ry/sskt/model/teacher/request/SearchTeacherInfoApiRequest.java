package com.ry.sskt.model.teacher.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.common.request.RequestBase;
import com.ry.sskt.model.student.entity.QuestionAnswer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 模糊搜索所有教师名称Request
 */
@Data
@Accessors(chain = true)
public class SearchTeacherInfoApiRequest extends RequestBase {
    /// <summary>
    /// 教师名称
    /// </summary>
    @JSONField(name = "TeacherName")
    private String teacherName;

}
