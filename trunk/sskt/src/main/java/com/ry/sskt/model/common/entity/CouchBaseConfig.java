package com.ry.sskt.model.common.entity;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * Couchbase缓存连接串表
 * </p>
 *
 * @author xrq
 * @since 2020-04-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CouchBaseConfig {
    private static final long serialVersionUID = 5716226141919572394L;
    /**
     * 唯一ID
     */
    @JSONField(name = "CouchBaseConfigID")
    private int couchBaseConfigID;

    /**
     * 名称
     */
    @JSONField(name = "CouchBaseConfigName")
    private String couchBaseConfigName;

    /**
     * 连接串值
     */
    @JSONField(name = "CouchBaseConfigValue")
    private String couchBaseConfigValue;

    /**
     * 是否启用
     */
    @JSONField(name = "Enable")
    private int enable;

}
