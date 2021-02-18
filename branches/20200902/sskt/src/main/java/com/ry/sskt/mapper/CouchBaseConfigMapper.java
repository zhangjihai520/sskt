package com.ry.sskt.mapper;

import com.ry.sskt.model.common.entity.CouchBaseConfig;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

public interface CouchBaseConfigMapper{
    /**
     * 查询所有有效的couchbaseconfig配置
     *
     * @return couchbaseconfig集合
     */
    @Select("call VideoClassMain_SP_CouchBaseConfig_ReadAll()")
    @Options(statementType = StatementType.CALLABLE)
    List<CouchBaseConfig> couchBaseConfigs();

}
