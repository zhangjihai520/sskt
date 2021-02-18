package com.ry.sskt.service;

import com.ry.sskt.model.common.entity.User;
import com.ry.sskt.model.subject.entity.SubjectRoom;
import com.ry.sskt.model.subject.entity.view.SubjectListByFilterView;
import com.ry.sskt.model.subject.entity.view.SubjectRoomTeacherNameView;

import java.util.List;

/**
 * 表SubjectRoom的接口
 */
public interface ISubjectRoomService {

    /// <summary>
    /// 新增或者更新一条SubjectRoom表数据
    /// </summary>
    /// <param name="dataModel">实体类</param>
    /// <returns>新增或更新后的主键Id</returns>
    int save(SubjectRoom dataModel) throws Exception;

    /// <summary>
    /// 根据主键获取一条SubjectRoom表数据
    /// </summary>
    /// <param name="subjectRoomId">教室id</param>
    /// <returns>查询到的表实体对象</returns>
    SubjectRoom getByKey(int subjectRoomId);

    /// <summary>
    /// 获取课程下的教室信息列表
    /// </summary>
    /// <param name="subjectId"></param>
    /// <returns></returns>
    List<SubjectRoomTeacherNameView> getSubjectRoomList(int subjectId);

    /// <summary>
    /// 获取某学生已报名某课程的教室
    /// </summary>
    /// <param name="subjectId">课程id</param>
    /// <param name="studentId">学生id</param>
    /// <returns>查询到的表实体对象</returns>
    SubjectRoom getSubjectRoomByStudentId(int subjectId, int studentId);

    /// <summary>
    /// 获取某老师已旁听某课程的教室
    /// </summary>
    /// <param name="subjectId">课程id</param>
    /// <param name="studentId">学生id</param>
    /// <returns>查询到的表实体对象</returns>
    SubjectRoom getSubjectRoomByTeacherId(int subjectId, int teacherId);

    /// <summary>
    /// 刷新实际报名人数
    /// </summary>
    /// <param name="subjectRoomId"></param>
    void refreshRegisterNumber(int subjectRoomId);

    /// <summary>
    /// 删除教室
    /// </summary>
    /// <param name="classRoomIds"></param>
    void deleteRooms(List<Integer> classRoomIds);


    /// <summary>
    /// 清除指定教室用户缓存
    /// </summary>
    /// <param name="subjectRoomId"></param>
    void clearSubjectRoomUserListCache(int subjectRoomId);
}
