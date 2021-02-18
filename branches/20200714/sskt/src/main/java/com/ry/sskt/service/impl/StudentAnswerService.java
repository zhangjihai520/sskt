package com.ry.sskt.service.impl;

import com.ry.sskt.mapper.StudentAnswerMapper;
import com.ry.sskt.model.examset.entity.StudentAnswer;
import com.ry.sskt.service.IStudentAnswerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
        return studentAnswerMapper.save(dataModel);
    }

    @Override
    public StudentAnswer getByKey(int examSetId, int userId) {
        return studentAnswerMapper.getByKey(examSetId, userId);
    }

    @Override
    public List<StudentAnswer> getStudentAnswerBySubjectId(int userid, String subjectIds) {
        if (StringUtils.isBlank(subjectIds)) {
            return null;
        }
        return studentAnswerMapper.getStudentAnswerBySubjectId(userid, subjectIds);
    }
}
