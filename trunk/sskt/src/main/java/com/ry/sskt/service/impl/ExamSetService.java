package com.ry.sskt.service.impl;

import com.ry.sskt.core.utils.RedisUtil;
import com.ry.sskt.mapper.ExamSetMapper;
import com.ry.sskt.model.common.constant.CommonConst;
import com.ry.sskt.model.common.entity.TwoTuple;
import com.ry.sskt.model.common.entity.User;
import com.ry.sskt.model.examset.entity.ExamSet;
import com.ry.sskt.model.examset.view.ExamSetListView;
import com.ry.sskt.model.examset.view.GetStatistListView;
import com.ry.sskt.service.IExamSetService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ExamSetService implements IExamSetService {

    @Autowired
    ExamSetMapper examSetMapper;

    @Override
    public int save(ExamSet dataModel) {
        if (dataModel.getExamSetId() > 0) {
            ExamSet examSet = new ExamSet().setExamSetId(dataModel.getExamSetId());
            if (examSet != null) {
                RedisUtil.del(examSet.getKey());
            }
        }
        return examSetMapper.save(dataModel);
    }

    @Override
    public ExamSet getByKey(int examSetId) {
        ExamSet cache = RedisUtil.getObj(new ExamSet().setExamSetId(examSetId).getKey(), ExamSet.class);
        if (cache == null) {
            cache = examSetMapper.getByKey(examSetId);
            if (cache != null) {
                RedisUtil.setObj(cache, CommonConst.MINUTE_480);
            }
        }
        return cache;
    }

    @Override
    public TwoTuple<Integer, List<ExamSetListView>> getExamSetListForTeacher(int subjectId, int courseId, String examSetName, int statusFlag, int pageSize, int pageIndex) {
        Map<String, Object> map = new HashMap<>();
        map.put("subjectId", subjectId);
        map.put("courseId", courseId);
        map.put("statusFlag", statusFlag);
        map.put("examSetName", examSetName);
        map.put("pageSkip", (pageIndex - 1) * pageSize);
        map.put("pageSize", pageSize);
        map.put("totalCount", 0);
        List<ExamSetListView> lists = examSetMapper.getExamSetListForTeacher(map);
        Integer total = Integer.parseInt(map.get("totalCount").toString());
        return new TwoTuple<Integer, List<ExamSetListView>>(total, lists);
    }

    @Override
    public TwoTuple<Integer, List<GetStatistListView>> getStatistList(int examSetId, int subjectRoomId, String userTrueName, int pageSize, int pageIndex) {
        Map<String, Object> map = new HashMap<>();
        map.put("examSetId", examSetId);
        map.put("subjectRoomId", subjectRoomId);
        map.put("userTrueName", userTrueName);
        map.put("pageSkip", (pageIndex - 1) * pageSize);
        map.put("pageSize", pageSize);
        map.put("totalCount", 0);
        List<GetStatistListView> lists = examSetMapper.getStatistList(map);
        Integer total = Integer.parseInt(map.get("totalCount").toString());
        return new TwoTuple<Integer, List<GetStatistListView>>(total, lists);
    }

    @Override
    public List<ExamSet> getExamSetList(int subjectId) {
        return examSetMapper.getExamSetList(subjectId);
    }

    @Override
    public List<ExamSet> getExamSetList(List<Integer> subjectIds) {
        List result = new LinkedList<ExamSet>();
        int maxLength = 3950;
        String listJoinStr = StringUtils.join(subjectIds, ",");
        while (listJoinStr.length() > maxLength) {
            String theIdListString = listJoinStr.substring(0, maxLength);
            int lastIndex = theIdListString.lastIndexOf(',');
            if (lastIndex > 0) {
                theIdListString = theIdListString.substring(0, lastIndex);
                listJoinStr = listJoinStr.substring(lastIndex + 1);
            }
            result.addAll(examSetMapper.getAllBySubjectIdList(theIdListString));
        }
        if (listJoinStr.length() > 0) {
            result.addAll(examSetMapper.getAllBySubjectIdList(listJoinStr));
        }
        return result;
    }

    @Override
    public int deleteExamSetList(List<Integer> examSetIds) {
        if (CollectionUtils.isNotEmpty(examSetIds)) {
            examSetIds.forEach(x -> {
                if (x != null) {
                    ExamSet examSet = new ExamSet().setExamSetId(x.intValue());
                    RedisUtil.del(examSet.getKey());
                }
            });
        }
        String str = StringUtils.join(examSetIds, ",");
        return examSetMapper.deleteExamSetList(str);
    }

    @Override
    public ExamSet getByMotkExamSetID(String motkExamSetID) {
        if (StringUtils.isBlank(motkExamSetID)) {
            return null;
        }
        return examSetMapper.getByMotkExamSetID(motkExamSetID);
    }

    @Override
    public int getCountForTypeId(int subjectId, int examSetTypeId) {
        return examSetMapper.getCountForTypeId(subjectId, examSetTypeId);
    }
}
