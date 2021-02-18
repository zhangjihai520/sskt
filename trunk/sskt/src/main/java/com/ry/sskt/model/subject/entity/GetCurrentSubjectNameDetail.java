package com.ry.sskt.model.subject.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

@Data
@Accessors(chain = true)
public class GetCurrentSubjectNameDetail {
    /// <summary>
    /// 课程加密ID
    /// </summary>
    private String SubjectId;

    /// <summary>
    /// 课程名称
    /// </summary>
    private String SubjectName;
}
