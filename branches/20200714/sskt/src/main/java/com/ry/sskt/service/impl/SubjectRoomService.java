package com.ry.sskt.service.impl;

import com.ry.sskt.core.utils.RedisUtil;
import com.ry.sskt.mapper.SubjectRoomMapper;
import com.ry.sskt.model.discuss.entity.cache.SubjectRoomUserListCouchBaseObject;
import com.ry.sskt.model.subject.entity.SubjectRoom;
import com.ry.sskt.model.subject.entity.view.SubjectRoomTeacherNameView;
import com.ry.sskt.service.ISubjectRoomService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectRoomService implements ISubjectRoomService {
    @Autowired
    SubjectRoomMapper subjectRoomMapper;
    @Override
    public int save(SubjectRoom dataModel) throws Exception {
        if (dataModel == null) {
            throw new Exception("dataModel null");
        }
        return subjectRoomMapper.save(dataModel);
    }

    @Override
    public SubjectRoom getByKey(int subjectRoomId) {
        return subjectRoomMapper.getByKey(subjectRoomId);
    }

    @Override
    public List<SubjectRoomTeacherNameView> getSubjectRoomList(int subjectId) {
        return subjectRoomMapper.getSubjectRoomList(subjectId);
    }

    @Override
    public SubjectRoom getSubjectRoomByStudentId(int subjectId, int studentId) {
        return subjectRoomMapper.getSubjectRoomByStudentId(subjectId, studentId);
    }

    @Override
    public SubjectRoom getSubjectRoomByTeacherId(int subjectId, int teacherId) {
        return subjectRoomMapper.getSubjectRoomByTeacherId(subjectId, teacherId);
    }

    @Override
    public void refreshRegisterNumber(int subjectRoomId) {
        subjectRoomMapper.refreshRegisterNumber(subjectRoomId);
    }

    @Override
    public void deleteRooms(List<Integer> classRoomIds) {
        String str = StringUtils.join(classRoomIds.toArray(), ",");
        subjectRoomMapper.deleteRooms(str);
    }

    @Override
    public void clearSubjectRoomUserListCache(int subjectRoomId) {
        SubjectRoomUserListCouchBaseObject cache = new SubjectRoomUserListCouchBaseObject().setSubjectRoomId(subjectRoomId);
        RedisUtil.del(cache.getKey());
    }
}
