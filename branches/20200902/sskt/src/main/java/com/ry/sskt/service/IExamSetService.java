package com.ry.sskt.service;

import com.ry.sskt.model.common.entity.TwoTuple;
import com.ry.sskt.model.examset.entity.ExamSet;
import com.ry.sskt.model.examset.view.ExamSetListView;
import com.ry.sskt.model.examset.view.GetStatistListView;

import java.util.List;

/**
 * 作业业务层接口
 */
public interface IExamSetService {
    /// <summary>
    /// 新增或者更新一条ExamSet表数据
    /// </summary>
    /// <param name="dataModel">实体类</param>
    /// <returns>新增或更新后的主键Id</returns>
    int save(ExamSet dataModel);

    /// <summary>
    /// 根据主键获取一条ExamSet表数据
    /// </summary>
    /// <param name="examSetId">作业id</param>
    /// <returns>查询到的表实体对象</returns>
    ExamSet getByKey(int examSetId);

    /// <summary>
    /// 获取主教老师的课堂作业列表
    /// </summary>
    /// <param name="subjectId"></param>
    /// <param name="courseId"></param>
    /// <param name="examSetName"></param>
    /// <param name="statusFlag"></param>
    /// <param name="pageSize"></param>
    /// <param name="pageIndex"></param>
    /// <returns></returns>
    TwoTuple<Integer, List<ExamSetListView>> getExamSetListForTeacher(int subjectId, int courseId, String examSetName, int statusFlag, int pageSize, int pageIndex);

    /// <summary>
    /// 获取作业统计列表
    /// </summary>
    /// <param name="examSetId"></param>
    /// <param name="subjectRoomId"></param>
    /// <param name="userTrueName"></param>
    /// <param name="pageSize"></param>
    /// <param name="pageIndex"></param>
    /// <returns></returns>
    TwoTuple<Integer, List<GetStatistListView>> getStatistList(int examSetId, int subjectRoomId, String userTrueName, int pageSize, int pageIndex);

    /// <summary>
    /// 获取课程下面的所有作业
    /// </summary>
    /// <param name="subjectId"></param>
    /// <returns></returns>
    List<ExamSet> getExamSetList(int subjectId);

    /// <summary>
    /// 批量获取课程下面的所有作业
    /// </summary>
    /// <param name="subjectId"></param>
    /// <returns></returns>
    List<ExamSet> getExamSetList(List<Integer> subjectId);

    /// <summary>
    /// 删除作业
    /// </summary>
    /// <param name="examSetIds"></param>
    /// <returns></returns>
    int deleteExamSetList(List<Integer> examSetIds);

    /// <summary>
    /// 根据MotkExamSetID获取一条ExamSet表数据
    /// </summary>
    /// <param name="examSetId">作业id</param>
    /// <returns>查询到的表实体对象</returns>
    ExamSet getByMotkExamSetID(String motkExamSetID);

    /// <summary>
    /// 根据状态获取作业数量
    /// </summary>
    /// <param name="subjectId"></param>
    /// <param name="examSetTypeId"></param>
    /// <returns></returns>
    int getCountForTypeId(int subjectId, int examSetTypeId);
}
