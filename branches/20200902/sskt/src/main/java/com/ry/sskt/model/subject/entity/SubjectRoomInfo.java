package com.ry.sskt.model.subject.entity;

import com.ry.sskt.core.utils.UrlUtil;
import com.ry.sskt.model.subject.entity.view.SubjectRoomTeacherNameView;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

@Data
@Accessors(chain = true)
public class SubjectRoomInfo {
    /// <summary>
    /// 教室ID
    /// </summary>
    private String SubjectRoomId;

    /// <summary>
    /// 教室名称
    /// </summary>
    private String TeacherName;

    /// <summary>
    /// 获取实例
    /// </summary>
    /// <param name="view"></param>
    /// <returns></returns>
    public SubjectRoomInfo(SubjectRoomTeacherNameView view) {

        SubjectRoomId = UrlUtil.encrypt(view.getSubjectRoomId());
        TeacherName = view.getUserTrueName();
    }
}
