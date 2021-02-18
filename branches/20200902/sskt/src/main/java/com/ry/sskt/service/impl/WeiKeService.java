package com.ry.sskt.service.impl;

import com.ry.sskt.mapper.WeiKeMapper;
import com.ry.sskt.model.common.entity.TwoTuple;
import com.ry.sskt.model.student.entity.UserMessageInfo;
import com.ry.sskt.model.subject.entity.WeiKe;
import com.ry.sskt.service.IWeiKeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 微课 服务实现类
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
@Service
public class WeiKeService implements IWeiKeService {
    @Autowired
    WeiKeMapper weiKeMapper;

    @Override
    public int Save(WeiKe dataModel) throws Exception {
        if (dataModel == null) {
            throw new Exception("保存微课信息入参为空");
        }
        return weiKeMapper.Save(dataModel);
    }

    @Override
    public WeiKe GetByKey(int weiKeId) {
        return weiKeMapper.GetByKey(weiKeId);
    }

    @Override
    public TwoTuple<Integer, List<WeiKe>> GetListBySearch(int statusFlag, int gradeId, int courseId, LocalDateTime beginTime, LocalDateTime endTime, String keyword, int teacherId, String statusFlagSymbol, int pageIndex, int pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("statusFlag", statusFlag);
        map.put("gradeId", gradeId);
        map.put("courseId", courseId);
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);
        map.put("keyword", keyword);
        map.put("teacherId", teacherId);
        map.put("statusFlagSymbol", statusFlagSymbol);
        map.put("pageSkip", (pageIndex - 1) * pageSize);
        map.put("pageSize", pageSize);
        map.put("totalCount", 0);
        List<WeiKe> lists = weiKeMapper.GetListBySearch(map);
        int total = Integer.parseInt(map.get("totalCount").toString());
        return new TwoTuple<Integer, List<WeiKe>>(total, lists);
    }

    @Override
    public boolean UpdateWeiKeStatus(List<Integer> ids, int statusFlag) {
        String joinIds = StringUtils.join(ids, ",");
        weiKeMapper.UpdateWeiKeStatus(joinIds, statusFlag);
        return true;
    }

    @Override
    public int UpdateFilePath(int weikeId, String filePath) {
        if (filePath == null) {
            filePath = "";
        }
        return weiKeMapper.UpdateFilePath(weikeId, filePath);
    }

    @Override
    public int UpdatePPTFilePath(int weikeId, String pptFilePath) {
        if (pptFilePath == null) {
            pptFilePath = "";
        }
        return weiKeMapper.UpdatePPTFilePath(weikeId, pptFilePath);
    }
}
