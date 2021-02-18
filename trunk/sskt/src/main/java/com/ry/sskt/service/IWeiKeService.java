package com.ry.sskt.service;

import com.ry.sskt.model.common.entity.TwoTuple;
import com.ry.sskt.model.subject.entity.WeiKe;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 微课 服务类
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
public interface IWeiKeService{
    /// <summary>
    /// 新增或者更新一条WeiKe表数据
    /// </summary>
    /// <param name="dataModel">实体类</param>
    /// <returns>新增或更新后的主键Id</returns>
    int Save(WeiKe dataModel) throws Exception;

    /// <summary>
    /// 根据主键获取一条WeiKe表数据
    /// </summary>
    /// <param name="weiKeId">微课id</param>
    /// <returns>查询到的表实体对象</returns>
    WeiKe GetByKey(int weiKeId);


    /// <summary>
    /// 获取微课列表
    /// </summary>
    /// <param name="statusFlag"></param>
    /// <param name="gradeId"></param>
    /// <param name="courseId"></param>
    /// <param name="beginTime"></param>
    /// <param name="endTime"></param>
    /// <param name="keyword"></param>
    /// <param name="teacherId">上传教师Id，为0则全部</param>
    /// <param name="statusFlagSymbol">用于判断状态符号</param>
    /// <param name="pageIndex"></param>
    /// <param name="pageSize"></param>
    /// <returns></returns>
    TwoTuple<Integer, List<WeiKe>> GetListBySearch(int statusFlag, int gradeId, int courseId, LocalDateTime beginTime,
                                                   LocalDateTime endTime, String keyword, int teacherId, String statusFlagSymbol, int pageIndex, int pageSize);

    /// <summary>
    /// 更新微课状态
    /// </summary>
    /// <param name="ids"></param>
    /// <param name="statusFlag"></param>
    /// <returns></returns>
    boolean UpdateWeiKeStatus(List<Integer> ids, int statusFlag);

    /// <summary>
    /// 更新主线路视频地址
    /// </summary>
    /// <param name="weikeId"></param>
    /// <param name="filePath"></param>
    /// <returns></returns>
    int UpdateFilePath(int weikeId, String filePath);

    /// <summary>
    /// 更新PPT线路视频地址
    /// </summary>
    /// <param name="weikeId"></param>
    /// <param name="pptFilePath"></param>
    /// <returns></returns>
    int UpdatePPTFilePath(int weikeId, String pptFilePath);
}
