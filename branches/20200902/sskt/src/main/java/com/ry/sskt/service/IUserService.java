package com.ry.sskt.service;

import com.ry.sskt.model.common.entity.TwoTuple;
import com.ry.sskt.model.common.entity.User;
import com.ry.sskt.model.subject.entity.Subject;
import com.ry.sskt.model.subject.entity.view.SubjectListByFilterView;

import java.util.List;

/**
 * 表Subject的接口
 */
public interface IUserService {

    /// <summary>
    /// 新增或者更新一条User表数据
    /// </summary>
    /// <param name="dataModel">实体类</param>
    /// <returns>新增或更新后的主键Id</returns>
    int save(User dataModel) throws Exception;

    /// <summary>
    /// 根据主键获取一条User表数据
    /// </summary>
    /// <param name="userId">用户id</param>
    /// <returns>查询到的表实体对象</returns>
    User getByKey(int userId, Boolean useCache);

    /// <summary>
    /// 搜索老师
    /// </summary>
    /// <param name="statusFlag"></param>
    /// <param name="keyword"></param>
    /// <param name="pageIndex"></param>
    /// <param name="pageSize"></param>
    /// <returns></returns>
    TwoTuple<Integer, List<User>> getListBySearch(int statusFlag, String keyword, int pageIndex, int pageSize);

    /**
     * 根据学籍号获取用户
     * @param studentCode
     * @return
     */
    User readByStudentCode(String studentCode);

    /// <summary>
    /// 批量保存User对象
    /// </summary>
    /// <param name="dataModels"></param>
    //int BatchInsertUser(List<User> dataModels);

    /// <summary>
    /// 根据来源类型和来源id获取用户
    /// </summary>
    /// <param name="sourceTypeId"></param>
    /// <param name="sourceId"></param>
    /// <returns></returns>
    User readBySource(int sourceTypeId, String sourceId);

    /// <summary>
    /// 清空用户表
    /// </summary>
    /// <returns></returns>
    void clear();

    /// <summary>
    /// 根据学籍号获取学生列表
    /// </summary>
    /// <param name="studentCodes"></param>
    /// <returns></returns>
    List<User> getListByStudentCodes(List<String> studentCodes);

    /// <summary>
    /// 根据来源类型和来源id批量删除用户
    /// </summary>
    /// <param name="sourceTypeId"></param>
    /// <param name="sourceIds"></param>
    /// <returns></returns>
    int deleteBySourceIds(int sourceTypeId, List<String> sourceIds);
}
