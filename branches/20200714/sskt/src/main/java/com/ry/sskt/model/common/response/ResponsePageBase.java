package com.ry.sskt.model.common.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: 幸仁强
 * @Date: 2019/7/16 15:34
 * @Description: 返回实体分页对象基类
 */
@Data
@Accessors(chain = true)
public class ResponsePageBase<T> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private List<T> dataList;//数据

    private long pageNo;//当前页

    private long pageSize;//条数

    private long total;//总条数

    private long pages;//总页面数目
}
