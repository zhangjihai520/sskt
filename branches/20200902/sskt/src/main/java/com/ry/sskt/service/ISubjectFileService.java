package com.ry.sskt.service;

import com.ry.sskt.model.subject.entity.SubjectFile;

import java.util.List;

/**
 * <p>
 * 课程附件 服务类
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
public interface ISubjectFileService{
    /// <summary>
    /// 新增或者更新一条SubjectFile表数据
    /// </summary>
    /// <param name="dataModel">实体类</param>
    /// <returns>新增或更新后的主键Id</returns>
    int Save(SubjectFile dataModel) throws Exception;

    /// <summary>
    /// 根据主键获取一条SubjectFile表数据
    /// </summary>
    /// <param name="subjectFileId">课程附件id</param>
    /// <returns>查询到的表实体对象</returns>
    SubjectFile GetByKey(int subjectFileId);

    /// <summary>
    /// 获取SubjectFile列表
    /// </summary>
    /// <param name="subjectId"></param>
    /// <returns></returns>
    List<SubjectFile> GetBySubjectId(int subjectId);

    /// <summary>
    /// 获取SubjectFile列表
    /// </summary>
    /// <param name="subjectIds"></param>
    /// <returns></returns>
    List<SubjectFile> GetBySubjectIds(List<Integer> subjectIds);

    /// <summary>
    /// 批量删除资料
    /// </summary>
    /// <param name="subjectFileIds"></param>
    /// <returns></returns>
    int DeleteSubjectFile(List<Integer> subjectFileIds);
}
