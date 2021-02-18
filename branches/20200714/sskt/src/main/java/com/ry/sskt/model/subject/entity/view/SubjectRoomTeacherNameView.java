package com.ry.sskt.model.subject.entity.view;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.sskt.model.subject.entity.SubjectRoom;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SubjectRoomTeacherNameView extends SubjectRoom {

    @JSONField(name = "UserTrueName")
    private String userTrueName;
}
