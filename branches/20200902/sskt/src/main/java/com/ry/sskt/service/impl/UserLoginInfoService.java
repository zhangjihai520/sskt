package com.ry.sskt.service.impl;

import com.ry.sskt.core.utils.RedisUtil;
import com.ry.sskt.mapper.UserLoginInfoMapper;
import com.ry.sskt.model.common.entity.UserLoginInfo;
import com.ry.sskt.model.common.entity.cache.UserLoginTotalCouchBaseObject;
import com.ry.sskt.model.common.entity.view.UserLoginTotalView;
import com.ry.sskt.service.IUserLoginInfoService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Date;
import java.util.List;

@Service
public class UserLoginInfoService implements IUserLoginInfoService {

    @Autowired
    UserLoginInfoMapper userLoginInfoMapper;

    @Override
    public void save(UserLoginInfo dataModel) throws Exception {
        if (dataModel == null) {
            throw new Exception("入参实体为空");
        }
        userLoginInfoMapper.save(dataModel);
    }


    @Override
    public List<UserLoginTotalView> getGradeTotal(LocalDate dateTime) {
        if (dateTime.isAfter(LocalDate.now())) {
            return getGradeTotal(dateTime, false);
        }
        return getGradeTotal(dateTime, true);
    }

    private List<UserLoginTotalView> getGradeTotal(LocalDate dateTime, Boolean useCache) {

        List<UserLoginTotalView> obj;
        UserLoginTotalCouchBaseObject cache = RedisUtil.getObj(new UserLoginTotalCouchBaseObject().setLoginDate(dateTime).getKey(), UserLoginTotalCouchBaseObject.class);
        if (cache == null) {
            obj = userLoginInfoMapper.getGradeTotal(dateTime);
            if (CollectionUtils.isNotEmpty(obj)) {
                RedisUtil.setObj(new UserLoginTotalCouchBaseObject(dateTime, obj));
            } else {
                obj = new LinkedList<>();
            }
            return obj;
        }
        return cache.getObjectList();
    }
}
