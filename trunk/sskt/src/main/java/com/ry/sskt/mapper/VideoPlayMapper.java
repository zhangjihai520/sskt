package com.ry.sskt.mapper;

import com.ry.sskt.model.common.entity.VideoPlayInfo;
import com.ry.sskt.model.common.entity.VideoPlayTotalView;
import com.ry.sskt.model.subject.entity.view.SubjectListByFilterView;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 表Videoplayinfo的 Mapper 接口
 * </p>
 *
 * @author xrq
 * @since 2020-04-20
 */
public interface VideoPlayMapper {

    /// <summary>
    /// 新增或者更新一条Videoplayinfo表数据
    /// </summary>
    /// <param name="dataModel">实体类</param>
    /// <returns>新增或更新后的主键ID</returns>
    @Select({
            "call Videoclassmain_SP_Videoplayinfo_Save(",
            "#{videoPlayInfo.subjectId},",
            "#{videoPlayInfo.videoUrl},",
            "#{videoPlayInfo.playDateTime},",
            "#{videoPlayInfo.playerId},",
            "#{videoPlayInfo.playTypeId})"
    })
    @Options(statementType = StatementType.CALLABLE)
    void save(@Param("videoPlayInfo") VideoPlayInfo videoPlayInfo);


    /// <summary>
    /// 获取某天课程播放次数
    /// </summary>
    /// <param name="map"></param>
    @Select({
            "call Videoclassmain_SP_Videoplayinfo_PlayCount(",
            "#{map.loginDateMax},",
            "#{map.loginDateMin})"
    })
    @Options(statementType = StatementType.CALLABLE)
    List<VideoPlayTotalView> getVideoPlayTotal(@Param("map") Map map);
}
