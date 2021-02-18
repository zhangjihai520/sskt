package com.ry.sskt.service;

import com.ry.sskt.model.common.entity.TwoTuple;
import com.ry.sskt.model.student.entity.EvaluateRecord;
import com.ry.sskt.model.student.entity.StudentEvaluateSubject;
import com.ry.sskt.model.student.entity.TeacherEvaluateSubject;
import com.ry.sskt.model.student.entity.view.GetAllSubjectEvaluateView;
import com.ry.sskt.model.student.entity.view.GetSubjectEvaluateView;
import com.ry.sskt.model.student.entity.view.TeacherToStudentEvaluateView;

import java.util.List;

/**
 * <p>
 * 评价表 服务类
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
public interface IEvaluateRecordService {
    /// <summary>
    /// 新增或者更新一条EvaluateRecord表数据
    /// </summary>
    /// <param name="dataModel">实体类</param>
    /// <returns>新增或更新后的主键Id</returns>
    int Save(EvaluateRecord dataModel) throws Exception;

    /// <summary>
    /// 根据主键获取一条EvaluateRecord表数据
    /// </summary>
    /// <param name="subjectId">课程id</param>
    /// <param name="sourseUserId">评价用户id</param>
    /// <param name="targetUserId">被评价用户id</param>
    /// <returns>查询到的表实体对象</returns>
    EvaluateRecord GetByKey(int subjectId, int sourseUserId, int targetUserId);

    /// <summary>
    /// 获取助教对课程评价列表
    /// </summary>
    /// <param name="teacherId"></param>
    /// <param name="pageIndex"></param>
    /// <param name="pageSize"></param>
    /// <returns></returns>
    TwoTuple<Integer, List<TeacherEvaluateSubject>> GetTeacherEvaluateSubjectList(int teacherId, int pageIndex, int pageSize, int isOnline);

    /// <summary>
    /// 获取学生对课程的评价列表
    /// </summary>
    /// <param name="studentId"></param>
    /// <param name="pageIndex"></param>
    /// <param name="pageSize"></param>
    /// <returns></returns>
    TwoTuple<Integer, List<StudentEvaluateSubject>> GetStudentEvaluateSubjectList(int studentId, int pageIndex, int pageSize, int isOnline);

    /// <summary>
    /// 获取老师对学生的评价
    /// </summary>
    /// <param name="studentId"></param>
    /// <param name="pageIndex"></param>
    /// <param name="pageSize"></param>
    /// <returns></returns>
    TwoTuple<Integer, List<TeacherToStudentEvaluateView>> GetTeacherToStudentEvaluateList(int studentId, int pageIndex, int pageSize, int isOnline);

    /// <summary>
    /// 获取主教老师所有课程的评价
    /// </summary>
    /// <param name="teacherId"></param>
    /// <param name="pageIndex"></param>
    /// <param name="pageSize"></param>
    /// <returns></returns>
    List<GetAllSubjectEvaluateView> GetAllSubjectEvaluate(int teacherId, int pageIndex, int pageSize, int isOnline);

    /// <summary>
    /// 获取用户对主讲老师的评价列表
    /// </summary>
    /// <param name="teacherId"></param>
    /// <param name="subjectId"></param>
    /// <param name="evaluateTypeId"></param>
    /// <param name="pageIndex"></param>
    /// <param name="pageSize"></param>
    /// <returns></returns>
    TwoTuple<Integer, List<GetSubjectEvaluateView>> GetSubjectEvaluate(int teacherId, int subjectId, int evaluateTypeId, int pageIndex, int pageSize, int isOnline);
}
