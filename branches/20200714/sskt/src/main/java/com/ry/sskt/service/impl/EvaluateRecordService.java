package com.ry.sskt.service.impl;

import com.ry.sskt.mapper.EvaluateRecordMapper;
import com.ry.sskt.model.common.entity.TwoTuple;
import com.ry.sskt.model.examset.view.ExamSetListView;
import com.ry.sskt.model.student.entity.EvaluateRecord;
import com.ry.sskt.model.student.entity.StudentEvaluateSubject;
import com.ry.sskt.model.student.entity.TeacherEvaluateSubject;
import com.ry.sskt.model.student.entity.view.GetAllSubjectEvaluateView;
import com.ry.sskt.model.student.entity.view.GetSubjectEvaluateView;
import com.ry.sskt.model.student.entity.view.TeacherToStudentEvaluateView;
import com.ry.sskt.service.IEvaluateRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评价表 服务实现类
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
@Service
public class EvaluateRecordService implements IEvaluateRecordService {

    @Autowired
    EvaluateRecordMapper evaluateRecordMapper;

    @Override
    public int Save(EvaluateRecord dataModel) throws Exception {
        if (dataModel == null) {
            throw new Exception("评价记录入参为空");
        }
        return evaluateRecordMapper.Save(dataModel);
    }

    @Override
    public EvaluateRecord GetByKey(int subjectId, int sourseUserId, int targetUserId) {
        return evaluateRecordMapper.GetByKey(subjectId, sourseUserId, targetUserId);
    }

    @Override
    public TwoTuple<Integer, List<TeacherEvaluateSubject>> GetTeacherEvaluateSubjectList(int teacherId, int pageIndex, int pageSize, int isOnline) {
        Map<String, Object> map = new HashMap<>();
        map.put("teacherId", teacherId);
        map.put("isOnline", isOnline);
        map.put("pageSkip", (pageIndex - 1) * pageSize);
        map.put("pageSize", pageSize);
        map.put("totalCount", 0);
        List<TeacherEvaluateSubject> lists = evaluateRecordMapper.GetTeacherEvaluateSubjectList(map);
        int total = Integer.parseInt(map.get("totalCount").toString());
        return new TwoTuple<Integer, List<TeacherEvaluateSubject>>(total, lists);
    }

    @Override
    public TwoTuple<Integer, List<StudentEvaluateSubject>> GetStudentEvaluateSubjectList(int studentId, int pageIndex, int pageSize, int isOnline) {
        Map<String, Object> map = new HashMap<>();
        map.put("studentId", studentId);
        map.put("isOnline", isOnline);
        map.put("pageSkip", (pageIndex - 1) * pageSize);
        map.put("pageSize", pageSize);
        map.put("totalCount", 0);
        List<StudentEvaluateSubject> lists = evaluateRecordMapper.GetStudentEvaluateSubjectList(map);
        int total = Integer.parseInt(map.get("totalCount").toString());
        return new TwoTuple<Integer, List<StudentEvaluateSubject>>(total, lists);
    }

    @Override
    public TwoTuple<Integer, List<TeacherToStudentEvaluateView>> GetTeacherToStudentEvaluateList(int studentId, int pageIndex, int pageSize, int isOnline) {
        Map<String, Object> map = new HashMap<>();
        map.put("studentId", studentId);
        map.put("isOnline", isOnline);
        map.put("pageSkip", (pageIndex - 1) * pageSize);
        map.put("pageSize", pageSize);
        map.put("totalCount", 0);
        List<TeacherToStudentEvaluateView> lists = evaluateRecordMapper.GetTeacherToStudentEvaluateList(map);
        int total = Integer.parseInt(map.get("totalCount").toString());
        return new TwoTuple<Integer, List<TeacherToStudentEvaluateView>>(total, lists);
    }

    @Override
    public List<GetAllSubjectEvaluateView> GetAllSubjectEvaluate(int teacherId, int pageIndex, int pageSize, int isOnline) {
        Map<String, Object> map = new HashMap<>();
        map.put("teacherId", teacherId);
        map.put("isOnline", isOnline);
        map.put("pageSkip", (pageIndex - 1) * pageSize);
        map.put("pageSize", pageSize);
        List<GetAllSubjectEvaluateView> lists = evaluateRecordMapper.GetAllSubjectEvaluate(map);
        return lists;
    }

    @Override
    public TwoTuple<Integer, List<GetSubjectEvaluateView>> GetSubjectEvaluate(int teacherId, int subjectId, int evaluateTypeId, int pageIndex, int pageSize, int isOnline) {
        Map<String, Object> map = new HashMap<>();
        map.put("teacherId", teacherId);
        map.put("subjectId", subjectId);
        map.put("evaluateTypeId", evaluateTypeId);
        map.put("isOnline", isOnline);
        map.put("pageSkip", (pageIndex - 1) * pageSize);
        map.put("pageSize", pageSize);
        map.put("totalCount", 0);
        List<GetSubjectEvaluateView> lists = evaluateRecordMapper.GetSubjectEvaluate(map);
        int total = Integer.parseInt(map.get("totalCount").toString());
        return new TwoTuple<Integer, List<GetSubjectEvaluateView>>(total, lists);
    }
}
