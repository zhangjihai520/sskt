package com.ry.sskt.mapper;

import com.ry.sskt.model.common.entity.User;
import com.ry.sskt.model.common.entity.UserLoginInfo;
import com.ry.sskt.model.common.entity.view.UserLoginTotalView;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表Mapper 接口
 * </p>
 *
 * @author xrq
 * @since 2020-04-20
 */
public interface UserLoginInfoMapper {

    /**
     * 获取所有公益课课程
     *
     * @return
     */
    @Select({
            "call Videoclassmain_SP_Userlogininfo_Save(",
            "#{model.userId,mode=IN,jdbcType=INTEGER},",
            "#{model.loginTime,mode=IN,jdbcType=DATE},",
            "#{model.loginDate,mode=IN,jdbcType=DATE},",
            "#{model.loginType,mode=IN,jdbcType=VARCHAR})"
    })
    @Options(statementType = StatementType.CALLABLE)
    void save(@Param("model") UserLoginInfo model);

    /// <summary>
    /// 获取某天登录学段用户数
    /// </summary>
    /// <param name="dateTime"></param>
    @Select("Call Videoclassmain_SP_Userlogininfo_GradeCount(#{datetime,mode=IN,jdbcType=DATE})")
    @Options(statementType = StatementType.CALLABLE)
    List<UserLoginTotalView> getGradeTotal(@Param("datetime") LocalDate datetime);

}
