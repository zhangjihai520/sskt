package com.ry.sskt.mapper;

import com.ry.sskt.model.subject.entity.WeiKe;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 微课 Mapper 接口
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
public interface WeiKeMapper {
    /// <summary>
    /// 新增或者更新一条WeiKe表数据
    /// </summary>
    /// <param name="dataModel">实体类</param>
    /// <returns>新增或更新后的主键Id</returns>
    @Select({
            "call VideoClassMain_SP_WeiKe_Save_V2(",
            "#{model.weiKeId,mode=IN,jdbcType=INTEGER},",
            "#{model.weiKeName,mode=IN,jdbcType=VARCHAR},",
            "#{model.teacherId,mode=IN,jdbcType=INTEGER},",
            "#{model.gradeId,mode=IN,jdbcType=INTEGER},",
            "#{model.courseId,mode=IN,jdbcType=INTEGER},",
            "#{model.filePath,mode=IN,jdbcType=VARCHAR},",
            "#{model.comment,mode=IN,jdbcType=VARCHAR},",
            "#{model.bookVersionId,mode=IN,jdbcType=INTEGER},",
            "#{model.courseMappingId,mode=IN,jdbcType=INTEGER},",
            "#{model.sectionId,mode=IN,jdbcType=INTEGER},",
            "#{model.statusFlag,mode=IN,jdbcType=INTEGER},",
            "#{model.overViewUrl,mode=IN,jdbcType=VARCHAR},",
            "#{model.pptFilePath,mode=IN,jdbcType=VARCHAR},",
            "#{model.sourceRoomId,mode=IN,jdbcType=VARCHAR})"
    })
    @Options(statementType = StatementType.CALLABLE)
    int Save(@Param("model") WeiKe model);

    /// <summary>
    /// 根据主键获取一条WeiKe表数据
    /// </summary>
    /// <param name="weiKeId">微课id</param>
    /// <returns>查询到的表实体对象</returns>
    @Select("Call VideoClassMain_SP_WeiKe_ReadByKey(#{weiKeId})")
    @Options(statementType = StatementType.CALLABLE)
    WeiKe GetByKey(@Param("weiKeId") int weiKeId);


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
    ///int statusFlag, int gradeId, int courseId, DateTime? beginTime,DateTime? endTime, string keyword, int teacherId, string statusFlagSymbol, int pageIndex, int pageSize
    @Select({
            "call VideoClassMain_SP_WeiKe_ReadBySearchV2(",
            "#{map.statusFlag,mode=IN,jdbcType=INTEGER},",
            "#{map.gradeId,mode=IN,jdbcType=INTEGER},",
            "#{map.courseId,mode=IN,jdbcType=INTEGER},",
            "#{map.beginTime,mode=IN,jdbcType=DATE},",
            "#{map.endTime,mode=IN,jdbcType=DATE},",
            "#{map.keyword,mode=IN,jdbcType=VARCHAR},",
            "#{map.teacherId,mode=IN,jdbcType=INTEGER},",
            "#{map.statusFlagSymbol,mode=IN,jdbcType=VARCHAR},",
            "#{map.pageSkip,mode=IN,jdbcType=INTEGER},",
            "#{map.pageSize,mode=IN,jdbcType=INTEGER},",
            "#{map.totalCount,mode=OUT,jdbcType=INTEGER})"
    })
    @Options(statementType = StatementType.CALLABLE)
    List<WeiKe> GetListBySearch(@Param("map") Map map);

    /// <summary>
    /// 更新微课状态
    /// </summary>
    /// <param name="ids"></param>
    /// <param name="statusFlag"></param>
    /// <returns></returns>
    @Select("Call VideoClassMain_SP_WeiKe_UpdateStatus(#{ids},#{statusFlag})")
    @Options(statementType = StatementType.CALLABLE)
    void UpdateWeiKeStatus(@Param("ids") String ids, @Param("statusFlag") int statusFlag);//int

    /// <summary>
    /// 更新主线路视频地址
    /// </summary>
    /// <param name="weikeId"></param>
    /// <param name="filePath"></param>
    /// <returns></returns>
    @Select("Call VideoClassMain_SP_WeiKe_UpdateFilePath(#{weikeId},#{filePath})")
    @Options(statementType = StatementType.CALLABLE)
    int UpdateFilePath(@Param("weikeId") int weikeId, @Param("filePath") String filePath);

    /// <summary>
    /// 更新PPT线路视频地址
    /// </summary>
    /// <param name="weikeId"></param>
    /// <param name="pptFilePath"></param>
    /// <returns></returns>
    @Select("Call VideoClassMain_SP_WeiKe_UpdatePPTFilePath(#{weikeId},#{pptFilePath})")
    @Options(statementType = StatementType.CALLABLE)
    int UpdatePPTFilePath(@Param("weikeId") int weikeId, @Param("pptFilePath") String pptFilePath);
}
