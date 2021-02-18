package com.ry.sskt.service;

import com.ry.sskt.model.common.entity.User;
import com.ry.sskt.model.student.entity.SubjectRoomStudent;
import com.ry.sskt.model.student.entity.view.GetStudentListView;
import com.ry.sskt.model.student.entity.view.StudentListByGroupView;
import com.ry.sskt.model.student.entity.view.SubjectStudentCountView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 学生报名表 服务类
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
public interface ISubjectRoomStudentService{
    /// <summary>
    /// 新增或者更新一条SubjectRoomStudent表数据
    /// </summary>
    /// <param name="dataModel">实体类</param>
    /// <returns>新增或更新后的主键Id</returns>
    int Save(SubjectRoomStudent dataModel) throws Exception;

    /// <summary>
    /// 根据主键获取一条SubjectRoomStudent表数据
    /// </summary>
    /// <param name="subjectRoomId">教室id</param>
    /// <param name="userId">学生id</param>
    /// <returns>查询到的表实体对象</returns>
    SubjectRoomStudent GetByKey(int subjectRoomId, int userId);

    /// <summary>
    /// 根据课程Id和学生Id获取教师学生信息
    /// </summary>
    /// <param name="subjectId"></param>
    /// <param name="userId"></param>
    /// <returns></returns>
    SubjectRoomStudent GetByUserIdAndSubjectId(int subjectId, int userId);

    /// <summary>
    /// 获取学生课程数
    /// </summary>
    /// <param name="userId">用户id</param>
    /// <returns>课程数量</returns>
    int GetSubjectCountByUserId(int userId);

    /// <summary>
    /// 获取学生作业数
    /// </summary>
    /// <param name="userId">用户id</param>
    /// <returns>作业数量</returns>
    int GetExamSetCountByUserId(int userId);

    /// <summary>
    /// 获取目标天数内的课程学生上课数量
    /// </summary>
    /// <param name="teacherId"></param>
    /// <param name="days"></param>
    /// <returns></returns>
    List<SubjectStudentCountView> GetStudentCountByTeacherId(int teacherId, LocalDate beginDate, LocalDate endDate, int isOnline);

    /// <summary>
    /// 更新学生课程状态
    /// </summary>
    /// <param name="subjectId"></param>
    /// <param name="userId"></param>
    /// <returns></returns>
    boolean UpdateSubjectStudentStatus(int subjectId, int userId, int statusFlag);

    /// <summary>
    /// 老师获取指定课程或教室的学生列表
    /// </summary>
    /// <param name="teacherId"></param>
    /// <param name="subjectId"></param>
    /// <param name="subjectRoomId"></param>
    /// <param name="searchKey">学生名称</param>
    /// <param name="isGetAttendance">是否只获取上课学生</param>
    /// <param name="pageSkip"></param>
    /// <param name="pageSize"></param>
    /// <param name="total"></param>
    ///int teacherId,int subjectId,int subjectRoomId,string searchKey,bool isGetAttendance,int pageSkip,int pageSize ,ref int total
    /// <returns></returns>
    List<GetStudentListView> GetStudentList(Map<String,Object> map);

    /// <summary>
    /// 批量插入学生报名
    /// </summary>
    /// <param name="dataModels"></param>
    /// <returns></returns>
    int BatchInsertSubjectRoomStudent(List<SubjectRoomStudent> dataModels);

    /// <summary>
    /// 获取学生分组列表
    /// </summary>
    /// <param name="subjectId"></param>
    /// <param name="subjectRoomId"></param>
    /// <param name="pageSkip"></param>
    /// <param name="pageSize"></param>
    /// <param name="totalCount"></param>
    /// <returns></returns>
    ///int subjectId, int subjectRoomId, int pageSkip, int pageSize, out int totalCount
    List<StudentListByGroupView> GetByGroupViews(Map<String,Object> map);

    /// <summary>
    /// 获取课程下的报名学生列表
    /// </summary>
    /// <param name="subjectId"></param>
    /// <returns></returns>
    List<SubjectRoomStudent> GetAllBySubjectId(int subjectId);

    /// <summary>
    /// 删除课程下面的所有学生
    /// </summary>
    /// <param name="subjectId"></param>
    /// <returns></returns>
    int DeleteAllBySubjectId(int subjectId);

    /// <summary>
    /// 获取课程教室下的报名学生及助教列表
    /// </summary>
    /// <param name="subjectRoomId"></param>
    /// <returns></returns>
    List<User> GetAllBySubjectRoomId(int subjectRoomId);

    /// <summary>
    /// 清除指定教室用户缓存
    /// </summary>
    /// <param name="subjectRoomId"></param>
    void ClearSubjectRoomUserListCache(int subjectRoomId);
}
