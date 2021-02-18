package com.ry.sskt.service.impl;

import com.ry.sskt.core.utils.RedisUtil;
import com.ry.sskt.mapper.StudentAnswerMapper;
import com.ry.sskt.model.common.constant.CommonConst;
import com.ry.sskt.model.examset.entity.ExamSet;
import com.ry.sskt.model.examset.entity.StudentAnswer;
import com.ry.sskt.service.IStudentAnswerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class StudentAnswerService implements IStudentAnswerService {

    @Autowired
    StudentAnswerMapper studentAnswerMapper;

    @Override
    public int save(StudentAnswer dataModel) throws Exception {
        if (dataModel == null) {
            throw new Exception("保存学生作答失败，入参为空");
        }
        if (dataModel.getExamSetId() > 0) {
            StudentAnswer answer = new StudentAnswer().setExamSetId(dataModel.getExamSetId()).setUserId(dataModel.getUserId());
            if (answer != null) {
                RedisUtil.del(answer.getKey());
            }
        }
        return studentAnswerMapper.save(dataModel);
    }

    @Override
    public StudentAnswer getByKey(int examSetId, int userId) {
        StudentAnswer cache = RedisUtil.getObj(new StudentAnswer().setExamSetId(examSetId).setUserId(userId).getKey(), StudentAnswer.class);
        if (cache == null) {
            cache = studentAnswerMapper.getByKey(examSetId, userId);
            if (cache != null) {
                RedisUtil.setObj(cache, CommonConst.MINUTE_480);
            }
        }
        return cache;
    }

    @Override
    public List<StudentAnswer> getStudentAnswerBySubjectIds(int userid, String subjectIds) {
        if (StringUtils.isBlank(subjectIds)) {
            return null;
        }
        return studentAnswerMapper.getStudentAnswerBySubjectIds(userid, subjectIds);
    }

    @Override
    public List<StudentAnswer> getStudentAnswerBySubjectId(int examSetId, int subjectId)
    {
        return studentAnswerMapper.getStudentAnswerBySubjectId(examSetId, subjectId);
    }

    @Override
    public List<StudentAnswer> getStudentAnswerBySubjectRoomId(int examSetId, int subjectRoomId)
    {
        return studentAnswerMapper.getStudentAnswerBySubjectRoomId(examSetId, subjectRoomId);
    }
}
