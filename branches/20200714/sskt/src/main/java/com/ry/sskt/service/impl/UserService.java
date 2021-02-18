package com.ry.sskt.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ry.sskt.core.utils.RedisUtil;
import com.ry.sskt.mapper.UserMapper;
import com.ry.sskt.model.common.constant.CommonConst;
import com.ry.sskt.model.common.entity.TwoTuple;
import com.ry.sskt.model.common.entity.User;
import com.ry.sskt.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService implements IUserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public int save(User model) throws Exception {
        if (model == null) {
            throw new Exception("保存用户入参为空");
        }
        int result = userMapper.save(model);
        if (model.getUserId() > 0) {
            RedisUtil.setObj(model, CommonConst.MINUTE_480);
        }
        return result;
    }

    @Override
    public User getByKey(int userId, Boolean useCache) {
        if (useCache == null) {
            useCache = true;
        }
        User cache = RedisUtil.getObj(new User().setUserId(userId).getKey(), User.class);
        if (cache == null) {
            cache = userMapper.getUserFromDB(userId);
            if (cache != null) {
                RedisUtil.setObj(cache, CommonConst.MINUTE_480);
            }
        }
        return cache;
    }

    @Override
    public TwoTuple<Integer, List<User>> getListBySearch(int statusFlag, String keyword, int pageIndex, int pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("statusFlag", statusFlag);
        map.put("keyword", keyword);
        map.put("pageSkip", (pageIndex - 1) * pageSize);
        map.put("pageSize", pageSize);
        map.put("totalCount", 0);
        System.out.println(JSON.toJSONString(map));
        List<User> users = userMapper.getListBySearch(map);
        Integer total = Integer.parseInt(map.get("totalCount").toString());
        return new TwoTuple<Integer, List<User>>(total, users);
    }

    @Override
    public User readByStudentCode(String studentCode) {
        return userMapper.readByStudentCode(studentCode);
    }

    /**
     * @Override public int BatchInsertUser(List<User> dataModels) {
     * return _dal.BatchInsertUser(dataModels);
     * }
     **/
    @Override
    public User readBySource(int sourceTypeId, String sourceId) {
        return userMapper.readBySource(sourceTypeId, sourceId);
    }

    @Override
    public void clear() {
        userMapper.clear();
    }

    @Override
    public List<User> getListByStudentCodes(List<String> studentCodes) {
        String str = "'" + StringUtils.join(studentCodes, "','") + "'";
        return userMapper.getListByStudentCodes(str);
    }

    @Override
    public int deleteBySourceIds(int sourceTypeId, List<String> sourceIds) {
        String str = "'" + StringUtils.join(sourceIds, "','") + "'";
        return userMapper.deleteBySourceIds(sourceTypeId, str);
    }
}
