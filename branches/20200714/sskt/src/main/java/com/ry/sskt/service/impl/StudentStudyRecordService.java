package com.ry.sskt.service.impl;

import com.ry.sskt.mapper.StudentStudyRecordMapper;
import com.ry.sskt.model.student.entity.StudentStudyRecord;
import com.ry.sskt.service.IStudentStudyRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 学生轨迹表 服务实现类
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
@Service
@Slf4j
public class StudentStudyRecordService implements IStudentStudyRecordService {

    @Autowired
    StudentStudyRecordMapper studentStudyRecordMapper;

    @Override
    public int Save(StudentStudyRecord dataModel) {
        if (dataModel == null) {
            log.info("保存学生轨迹入参为空");
        }
        return studentStudyRecordMapper.Save(dataModel);
    }

    @Override
    public StudentStudyRecord GetByKey(int studentStudyRecordId) {
        return studentStudyRecordMapper.GetByKey(studentStudyRecordId);
    }

    @Override
    public List<StudentStudyRecord> GetListByUserId(int userId, int isOnline) {
        return studentStudyRecordMapper.GetListByUserId(userId, isOnline);
    }
}
