package com.ry.sskt.service.impl;

import com.ry.sskt.mapper.CouchBaseConfigMapper;
import com.ry.sskt.model.common.entity.CouchBaseConfig;
import com.ry.sskt.service.ICouchBaseConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouchBaseConfigService implements ICouchBaseConfigService {
    @Autowired
    private CouchBaseConfigMapper couchBaseConfigMapper;

    @Override
    public List<CouchBaseConfig> couchBaseConfigs() {
        return couchBaseConfigMapper.couchBaseConfigs();
    }

}