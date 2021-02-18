package com.ry.sskt.model.common.constant;

import lombok.Getter;
import lombok.Setter;

/**
 * @Auther: 幸仁强
 * @Date: 2019/7/16 15:34
 * @Description: 获取热门数据类型
 */
public enum HotSubjectEnum {

    /**
     * 学生
     */
    Subject(1),

    /**
     * 教师
     */
    Teacher(2),

    /**
     * 学校
     */
    School(3);

    @Getter
    @Setter
    private Integer Code;

    // 构造方法
    HotSubjectEnum(int Code) {
        this.Code = Code;
    }

}
