package com.ry.sskt.model.subject.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

@Data
@Accessors(chain = true)
public class StudentRoom {
    /// <summary>
    /// 学生Id
    /// </summary>
    private int UserId;

    /// <summary>
    /// 教室ID
    /// </summary>
    private String SubjectRoomId;
}
