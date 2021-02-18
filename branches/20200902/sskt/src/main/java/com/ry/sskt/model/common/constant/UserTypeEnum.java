package com.ry.sskt.model.common.constant;

import lombok.Getter;
import lombok.Setter;

/**
 * @Auther: 幸仁强
 * @Date: 2019/7/16 15:34
 * @Description: 用户类型
 */
public enum UserTypeEnum {

    /**
     * 用于Action过滤
     */
    All(0),

    /**
     * 学生
     */
    Student(1),

    /**
     * 老师
     */
    Teacher(2),

    /**
     * 机构人员
     */
    Agent(3),

    /**
     * 家长
     */
    Parent (4);

    @Getter
    @Setter
    private int Code;

    // 构造方法
    UserTypeEnum(int Code) {
        this.Code = Code;
    }

}
