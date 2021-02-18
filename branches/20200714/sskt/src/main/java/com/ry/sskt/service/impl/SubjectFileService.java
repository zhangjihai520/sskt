package com.ry.sskt.service.impl;

import com.ry.sskt.mapper.SubjectFileMapper;
import com.ry.sskt.model.subject.entity.SubjectFile;
import com.ry.sskt.service.ISubjectFileService;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 课程附件 服务实现类
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
@Service
public class SubjectFileService implements ISubjectFileService {

    @Autowired
    SubjectFileMapper subjectFileMapper;

    @Override
    public int Save(SubjectFile dataModel) throws Exception {
        if (dataModel == null) {
            throw new Exception("保存课件附件入参为空");
        }
        return subjectFileMapper.Save(dataModel);
    }

    @Override
    public SubjectFile GetByKey(int subjectFileId) {
        return subjectFileMapper.GetByKey(subjectFileId);
    }

    @Override
    public List<SubjectFile> GetBySubjectId(int subjectId) {
        return subjectFileMapper.GetBySubjectId(subjectId);
    }

    @Override
    public List<SubjectFile> GetBySubjectIds(List<Integer> subjectIds) {
        var result = new LinkedList<SubjectFile>();
        var maxLength = 3950;
        String listJoinStr = StringUtils.join(subjectIds, ",");
        while (listJoinStr.length() > maxLength) {
            String theIdListString = listJoinStr.substring(0, maxLength);
            int lastIndex = theIdListString.lastIndexOf(',');
            if (lastIndex > 0) {
                theIdListString = theIdListString.substring(0, lastIndex);
                listJoinStr = listJoinStr.substring(lastIndex + 1);
            }
            result.addAll(subjectFileMapper.GetBySubjectIds(theIdListString));
        }
        if (listJoinStr.length() > 0) {
            result.addAll(subjectFileMapper.GetBySubjectIds(listJoinStr));
        }
        return result;
    }

    @Override
    public int DeleteSubjectFile(List<Integer> subjectFileIds) {
        String ids = StringUtils.join(subjectFileIds, ",") ;
        return subjectFileMapper.DeleteSubjectFile(ids);
    }
}
