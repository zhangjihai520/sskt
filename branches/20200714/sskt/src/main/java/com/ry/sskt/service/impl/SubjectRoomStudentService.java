package com.ry.sskt.service.impl;

import com.ry.sskt.core.utils.RedisUtil;
import com.ry.sskt.mapper.SubjectRoomStudentMapper;
import com.ry.sskt.model.common.constant.CommonConst;
import com.ry.sskt.model.common.entity.TwoTuple;
import com.ry.sskt.model.common.entity.User;
import com.ry.sskt.model.discuss.entity.cache.SubjectRoomUserListCouchBaseObject;
import com.ry.sskt.model.examset.view.GetStatistListView;
import com.ry.sskt.model.student.entity.SubjectRoomStudent;
import com.ry.sskt.model.student.entity.view.GetStudentListView;
import com.ry.sskt.model.student.entity.view.StudentListByGroupView;
import com.ry.sskt.model.student.entity.view.SubjectStudentCountView;
import com.ry.sskt.service.ISubjectRoomStudentService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 学生报名表 服务实现类
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
@Service
public class SubjectRoomStudentService implements ISubjectRoomStudentService {
    @Autowired
    SubjectRoomStudentMapper subjectRoomStudentMapper;

    @Override
    public int Save(SubjectRoomStudent dataModel) throws Exception {
        if (dataModel == null) {
            throw new Exception("保存学生报名信息入参为空");
        }
        return subjectRoomStudentMapper.Save(dataModel);
    }

    @Override
    public SubjectRoomStudent GetByKey(int subjectRoomId, int userId) {
        return subjectRoomStudentMapper.GetByKey(subjectRoomId, userId);
    }

    @Override
    public SubjectRoomStudent GetByUserIdAndSubjectId(int subjectId, int userId) {
        return subjectRoomStudentMapper.GetByUserIdAndSubjectId(subjectId, userId);
    }

    @Override
    public int GetSubjectCountByUserId(int userId) {
        return subjectRoomStudentMapper.GetSubjectCountByUserId(userId);
    }

    @Override
    public int GetExamSetCountByUserId(int userId) {
        return subjectRoomStudentMapper.GetExamSetCountByUserId(userId);
    }

    @Override
    public List<SubjectStudentCountView> GetStudentCountByTeacherId(int teacherId, LocalDate beginDate, LocalDate endDate, int isOnline) {
        return subjectRoomStudentMapper.GetStudentCountByTeacherId(teacherId, beginDate, endDate, isOnline);
    }

    @Override
    public boolean UpdateSubjectStudentStatus(int subjectId, int userId, int statusFlag) {
        return subjectRoomStudentMapper.UpdateSubjectStudentStatus(subjectId, userId, statusFlag);
    }

    @Override
    public List<GetStudentListView> GetStudentList(Map<String, Object> map) {
        return subjectRoomStudentMapper.GetStudentList(map);
    }


    @Override
    public int BatchInsertSubjectRoomStudent(List<SubjectRoomStudent> dataModels) {
        return subjectRoomStudentMapper.BatchInsertSubjectRoomStudent(dataModels);
    }


    @Override
    public List<StudentListByGroupView> GetByGroupViews(Map<String, Object> map) {
        return subjectRoomStudentMapper.GetByGroupViews(map);
    }

    @Override
    public List<SubjectRoomStudent> GetAllBySubjectId(int subjectId) {
        if (subjectId <= 0) {
            return new LinkedList<SubjectRoomStudent>();
        }
        return subjectRoomStudentMapper.GetAllBySubjectId(subjectId);
    }

    @Override
    public int DeleteAllBySubjectId(int subjectId) {
        if (subjectId <= 0) {
            return 0;
        }
        return subjectRoomStudentMapper.DeleteAllBySubjectId(subjectId);
    }

    @Override
    public void ClearSubjectRoomUserListCache(int subjectRoomId) {
        RedisUtil.del(new SubjectRoomUserListCouchBaseObject().setSubjectRoomId(subjectRoomId).getKey());
    }


    @Override
    public List<User> GetAllBySubjectRoomId(int subjectRoomId) {
        if (subjectRoomId <= 0) {
            return new LinkedList<User>();
        }
        SubjectRoomUserListCouchBaseObject cache = RedisUtil.getObj(new SubjectRoomUserListCouchBaseObject(subjectRoomId).getKey(), SubjectRoomUserListCouchBaseObject.class);
        if (cache == null) {
            cache = new SubjectRoomUserListCouchBaseObject(subjectRoomId);
            List<User> userList = subjectRoomStudentMapper.getAllBySubjectRoomId(subjectRoomId);
            if (CollectionUtils.isNotEmpty(userList)) {
                cache.setUserList(userList);
                RedisUtil.setObj(cache, CommonConst.MINUTE_1440);
            }
            return cache.getUserList();
        }
        return cache.getUserList();
    }
}
