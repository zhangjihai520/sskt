package com.ry.sskt.service;

import com.ry.sskt.model.examset.entity.StudentAnswer;
import com.ry.sskt.model.subject.entity.Subject;
import com.ry.sskt.model.subject.entity.view.SubjectListByFilterView;

import java.util.List;

/**
 * 表Subject的接口
 */
public interface IStudentAnswerService {

    /// <summary>
    /// 新增或者更新一条StudentAnswer表数据
    /// </summary>
    /// <param name="dataModel">实体类</param>
    /// <returns>新增或更新后的主键Id</returns>
    int save(StudentAnswer dataModel) throws Exception;

    /// <summary>
    /// 根据主键获取一条StudentAnswer表数据
    /// </summary>
    /// <param name="examSetId">作业id</param>
    /// <param name="userId">学生id</param>
    /// <returns>查询到的表实体对象</returns>
    StudentAnswer getByKey(int examSetId, int userId);

    /// <summary>
    /// 获取学生该课程下的所有作答
    /// </summary>
    /// <param name="userid"></param>
    /// <param name="subjectIds"></param>
    /// <returns></returns>
    List<StudentAnswer> getStudentAnswerBySubjectIds(int userid, String subjectIds);

    /// <summary>
    /// 根据课程ID获取该课程下的所有作答
    /// </summary>
    /// <param name="userid"></param>
    /// <param name="subjectIds"></param>
    /// <returns></returns>
    List<StudentAnswer> getStudentAnswerBySubjectId(int examSetId, int subjectId);


    /// <summary>
    /// 根据房间id获取该课程下的所有作答
    /// </summary>
    /// <param name="userid"></param>
    /// <param name="subjectIds"></param>
    /// <returns></returns>
    List<StudentAnswer> getStudentAnswerBySubjectRoomId(int examSetId, int subjectRoomId);

}
