package com.ry.sskt.service.impl;

import com.ry.sskt.core.config.CommonConfig;
import com.ry.sskt.core.utils.RedisUtil;
import com.ry.sskt.mapper.SubjectMapper;
import com.ry.sskt.model.common.constant.*;
import com.ry.sskt.model.student.entity.view.*;
import com.ry.sskt.model.subject.entity.Subject;
import com.ry.sskt.model.subject.entity.SubjectFile;
import com.ry.sskt.model.subject.entity.cache.AllPublicBenefitSubjectListCouchBaseObject;
import com.ry.sskt.model.subject.entity.view.SubjectListByFilterView;
import com.ry.sskt.model.video.entity.VHVideoView;
import com.ry.sskt.service.ISubjectService;
import lombok.var;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class SubjectService implements ISubjectService {

    @Autowired
    SubjectMapper subjectMapper;

    @Override
    public int Save(Subject dataModel) throws Exception {
        if (dataModel == null) {
            throw new Exception("保存课程信息入参为空");
        }
        return subjectMapper.save(dataModel);
    }

    /// <summary>
    /// 清除公益课缓存
    /// </summary>
    private void ClearPublicBenefitSubjectCache() {
        RedisUtil.del(new AllPublicBenefitSubjectListCouchBaseObject().getKey());
    }

    @Override
    public Subject getByKey(int subjectId) {
        return subjectMapper.getByKey(subjectId);
    }

    @Override
    public List<Subject> GetListByTeacherId(int teacherId, int isOnline) {
        return subjectMapper.getListByTeacherId(teacherId, isOnline);
    }

    @Override
    public List<SubjectListByFilterView> GetListByFilter(Map<String, Object> map) {
        return subjectMapper.GetListByFilter(map);
    }

    @Override
    public boolean UpdateStatusFlag(int subjectId, SubjectStatusFlagEnum status) {
        subjectMapper.updateStatusFlag(subjectId, status.getIndex());
        return true;
    }

    @Override
    public List<GetUserSubjectListH5View> GetUserSubjectListH5(LocalDate beginMonth, LocalDate endMonth, LocalDate beginDay, LocalDate endDay, int teacherId, int studentId, int isOnline, int subjectGenreId) {
        return subjectMapper.GetUserSubjectListH5(beginMonth, endMonth, beginDay, endDay, teacherId, studentId, isOnline, subjectGenreId);
    }

    @Override
    public List<GetStudentSubjectListH5View> GetStudentSubjectListH5(int studentId, int isOnline) {
        return subjectMapper.GetStudentSubjectListH5(studentId, isOnline);
    }

    @Override
    public List<GetPraiseView> GetPraise(List<Integer> subjectIds) {
        if (CollectionUtils.isEmpty(subjectIds)) {
            return null;
        }
        var result = new LinkedList<GetPraiseView>();
        var maxLength = 3950;
        String listJoinStr = StringUtils.join(subjectIds, ",");
        while (listJoinStr.length() > maxLength) {
            String theIdListString = listJoinStr.substring(0, maxLength);
            int lastIndex = theIdListString.lastIndexOf(',');
            if (lastIndex > 0) {
                theIdListString = theIdListString.substring(0, lastIndex);
                listJoinStr = listJoinStr.substring(lastIndex + 1);
            }
            result.addAll(subjectMapper.GetPraise(theIdListString));
        }
        if (listJoinStr.length() > 0) {
            result.addAll(subjectMapper.GetPraise(listJoinStr));
        }
        return result;
    }

    @Override
    public List<GetClassAttendanceView> GetClassAttendance(List<Integer> subjectIds) {
        if (CollectionUtils.isEmpty(subjectIds)) {
            return null;
        }
        var result = new LinkedList<GetClassAttendanceView>();
        var maxLength = 3950;
        String listJoinStr = StringUtils.join(subjectIds, ",");
        while (listJoinStr.length() > maxLength) {
            String theIdListString = listJoinStr.substring(0, maxLength);
            int lastIndex = theIdListString.lastIndexOf(',');
            if (lastIndex > 0) {
                theIdListString = theIdListString.substring(0, lastIndex);
                listJoinStr = listJoinStr.substring(lastIndex + 1);
            }
            result.addAll(subjectMapper.GetClassAttendance(theIdListString));
        }
        if (listJoinStr.length() > 0) {
            result.addAll(subjectMapper.GetClassAttendance(listJoinStr));
        }
        return result;
    }

    @Override
    public List<SearchStudentSubjectView> SearchStudentSubject(String key, int studentId, LocalDateTime minBeginTime, int isOnline) {
        return subjectMapper.SearchStudentSubject(key, studentId, minBeginTime, isOnline);
    }

    @Override
    public List<Subject> GetTeacherSubjectByDate(int teacherId, int helperTeacherId, LocalDate beginTime, LocalDate endDateTime, int isOnline) {
        return subjectMapper.GetTeacherSubjectByDate(teacherId, helperTeacherId, beginTime, endDateTime, isOnline);
    }

    @Override
    public List<SchoolStudentCountView> GetSchoolStudentCountView(int teacherId, int dataType, int isOnline) {
        switch (dataType) {
            case 1:
                return subjectMapper.GetSchoolStudentCountView(teacherId, isOnline);
            case 2:
                return subjectMapper.GetGradeStudentCountView(teacherId, isOnline);
        }
        return new LinkedList<SchoolStudentCountView>();
    }

    @Override
    public List<ExportSubjectListView> GetSubjectListBySubjectIds(List<Integer> subjectIds) {
        if (CollectionUtils.isEmpty(subjectIds)) {
            return null;
        }
        var result = new LinkedList<ExportSubjectListView>();
        var maxLength = 3950;
        String listJoinStr = StringUtils.join(subjectIds, ",");
        while (listJoinStr.length() > maxLength) {
            String theIdListString = listJoinStr.substring(0, maxLength);
            int lastIndex = theIdListString.lastIndexOf(',');
            if (lastIndex > 0) {
                theIdListString = theIdListString.substring(0, lastIndex);
                listJoinStr = listJoinStr.substring(lastIndex + 1);
            }
            result.addAll(subjectMapper.GetSubjectListBySubjectIds(theIdListString));
        }
        if (listJoinStr.length() > 0) {
            result.addAll(subjectMapper.GetSubjectListBySubjectIds(listJoinStr));
        }
        return result;
    }

    @Override
    public List<HotDataView> GetHotSubjectList(Map<String, Object> map) {
        return subjectMapper.GetHotSubjectList(map);
    }

    @Override
    public HotDataTotalView GetHotSubjectTotal(int teacherId, int isOnline) {
        return subjectMapper.GetHotSubjectTotal(teacherId, isOnline);
    }

    @Override
    public List<HotDataView> GetHotTeacherList(Map<String, Object> map) {
        return subjectMapper.GetHotTeacherList(map);
    }

    @Override
    public HotDataTotalView GetHotTeacherTotal(int isOnline) {
        return subjectMapper.GetHotTeacherTotal(isOnline);
    }

    @Override
    public List<HotDataView> GetHotSchoolList(Map<String, Object> map) {
        return subjectMapper.GetHotSchoolList(map);
    }

    @Override
    public HotDataTotalView GetHotSchoolTotal(int isOnline) {
        return subjectMapper.GetHotSchoolTotal(isOnline);
    }

    @Override
    public boolean UpdateSubjectStateTask() {
        subjectMapper.UpdateSubjectStateTask();
        return true;
    }


    @Override
    public int studentRegistSubject(int subjectId, int userId, boolean isAbsent) {
        return subjectMapper.studentRegistSubject(subjectId, userId, CommonConfig.getMaxRegisterNumber(), isAbsent ? 1 : 0);
    }

    @Override
    public int teacherRegistSubject(int subjectId, int userId, boolean isAbsent) {
        return subjectMapper.teacherRegistSubject(subjectId, userId, isAbsent ? 1 : 0);
    }

    @Override
    public List<GetStudentSubjectListView> GetStudentSubjectList(Map<String, Object> map) {
        return subjectMapper.GetStudentSubjectList(map);
    }

    @Override
    public GetTeacherSubjectCountView GetTeacherSubjectCount(int teacherId, LocalDateTime maxDateTime, LocalDateTime minDateTime, int isOnline) {
        return subjectMapper.GetTeacherSubjectCount(teacherId, maxDateTime, minDateTime, isOnline);
    }

    @Override
    public void UpdateFilePath(Subject dataModel) {
        subjectMapper.UpdateFilePath(dataModel);
    }

    @Override
    public void UpdatePPTFilePath(Subject dataModel) {
        subjectMapper.UpdatePPTFilePath(dataModel);
    }

    @Override
    public List<GetUserSubjectListH5View> GetALLSubjectListH5(LocalDate beginMonth, LocalDate endMonth, LocalDate beginDay, LocalDate endDay, int isOnline, int userId, int subjectGenreId) {
        return subjectMapper.GetALLSubjectListH5(beginMonth, endMonth, beginDay, endDay, isOnline, userId, subjectGenreId);
    }

    @Override
    public List<SearchSubjectView> SearchSubject(LocalDateTime beginTime, String key, int systemTypeId, int userId) {
        return subjectMapper.SearchSubject(beginTime, key, systemTypeId, userId);
    }

    @Override
    public void CloseSubjects(List<Integer> subjectIds) {
        if (CollectionUtils.isEmpty(subjectIds)) {
            subjectMapper.CloseSubjectsForAutomatic(LocalDateTime.now().plusDays(-1));
        } else {
            var result = new LinkedList<ExportSubjectListView>();
            var maxLength = 2000;
            String listJoinStr = StringUtils.join(subjectIds, ",");
            while (listJoinStr.length() > maxLength) {
                String theIdListString = listJoinStr.substring(0, maxLength);
                int lastIndex = theIdListString.lastIndexOf(',');
                if (lastIndex > 0) {
                    theIdListString = theIdListString.substring(0, lastIndex);
                    listJoinStr = listJoinStr.substring(lastIndex + 1);
                }
                subjectMapper.CloseSubjects(theIdListString);
            }
            if (listJoinStr.length() > 0) {
                subjectMapper.CloseSubjects(listJoinStr);
            }
        }
    }

    @Override
    public List<SubjectListByFilterView> getAllPublicBenefitSubject(boolean ifReadCache) {
        AllPublicBenefitSubjectListCouchBaseObject obj = null;
        if (ifReadCache) {
            obj = RedisUtil.getObj(new AllPublicBenefitSubjectListCouchBaseObject().getKey(), AllPublicBenefitSubjectListCouchBaseObject.class);
        }
        if (obj != null) {
            return obj.getAllPublicBenefitSubject();
        }
        List<SubjectListByFilterView> list = getAllPublicBenefitSubject();
        if (CollectionUtils.isNotEmpty(list)) {
            obj = new AllPublicBenefitSubjectListCouchBaseObject();
            obj.setAllPublicBenefitSubject(list);
            RedisUtil.setObj(obj, CommonConst.MINUTE_1440);
        }
        return obj.getAllPublicBenefitSubject();
    }

    /// <summary>
    /// 获取所有公益课，有缓存
    /// </summary>
    /// <returns></returns>
    private List<SubjectListByFilterView> getAllPublicBenefitSubject() {
        Map<String, Object> map = new HashMap<>();
        map.put("teacherId", 0);
        map.put("key", StringUtils.EMPTY);
        map.put("courseId", 0);
        map.put("gradeId", 0);
        map.put("beginTime", StringUtils.EMPTY);
        map.put("endTime", StringUtils.EMPTY);
        map.put("beginTimeMax", StringUtils.EMPTY);
        map.put("beginTimeMin", StringUtils.EMPTY);
        map.put("pageSkip", 0);
        map.put("pageSize", 1000);
        map.put("status", SubjectStatusFlagEnum.ToBeRegistered.getIndex());
        map.put("classState", SubjectClassStateEnum.All.getIndex());
        map.put("maxTimeField", StringUtils.EMPTY);
        map.put("minTimeField", StringUtils.EMPTY);
        map.put("isOnline", 0);
        map.put("subjectGenreId", SubjectGenreEnum.PublicBenefit.getIndex());
        map.put("OrderByField", SubjectListOrderFiledEnum.CreateDateTime_Desc.getMsg());
        map.put("totalCount", 0);
        List<SubjectListByFilterView> result = subjectMapper.GetListByFilter(map);
        return result;
    }


    @Override
    public void attendSubject(int subjectId, String rtmpPath) {
        subjectMapper.UpdateClassStatus(subjectId, rtmpPath, SubjectClassStateEnum.InClass.getIndex());
    }

    @Override
    public void closeSubject(int subjectId, String mp4Path) {
        subjectMapper.UpdateClassStatus(subjectId, mp4Path, SubjectClassStateEnum.ClosedClass.getIndex());
    }
}
