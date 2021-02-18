package com.ry.sskt.service;

import com.ry.sskt.model.student.entity.StudentStudyRecord;

import java.util.List;

/**
 * <p>
 * 学生轨迹表 服务类
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
public interface IStudentStudyRecordService{
    /// <summary>
    /// 新增或者更新一条StudentStudyRecord表数据
    /// </summary>
    /// <param name="dataModel">实体类</param>
    /// <returns>新增或更新后的主键Id</returns>
    int Save(StudentStudyRecord dataModel);

    /// <summary>
    /// 根据主键获取一条StudentStudyRecord表数据
    /// </summary>
    /// <param name="studentStudyRecordId">轨迹id</param>
    /// <returns>查询到的表实体对象</returns>
    StudentStudyRecord GetByKey(int studentStudyRecordId);

    /// <summary>
    /// 获取历史记录
    /// </summary>
    /// <param name="userId"></param>
    /// <returns></returns>
    List<StudentStudyRecord> GetListByUserId(int userId, int isOnline);
}
