package com.ry.sskt.model.common.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class IdName {
    @JSONField(name = "Id")
    private String id;
    @JSONField(name = "Name")
    private String name;

    public IdName() {
    }

    public IdName(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
