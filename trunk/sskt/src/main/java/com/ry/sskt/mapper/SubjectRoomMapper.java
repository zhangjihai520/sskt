package com.ry.sskt.mapper;

import com.ry.sskt.model.common.entity.User;
import com.ry.sskt.model.subject.entity.SubjectRoom;
import com.ry.sskt.model.subject.entity.view.SubjectListByFilterView;
import com.ry.sskt.model.subject.entity.view.SubjectRoomTeacherNameView;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程表 Mapper 接口
 * </p>
 *
 * @author xrq
 * @since 2020-04-20
 */
public interface SubjectRoomMapper {

    /// <summary>
    /// 新增或者更新一条SubjectRoom表数据
    /// </summary>
    /// <param name="dataModel">实体类</param>
    /// <returns>新增或更新后的主键Id</returns>
    @Select({
            "call VideoClassMain_SP_SubjectRoom_Save(",
            "#{dataModel.subjectRoomId,mode=IN,jdbcType=INTEGER},",
            "#{dataModel.subjectId,mode=IN,jdbcType=INTEGER},",
            "#{dataModel.teacherId,mode=IN,jdbcType=INTEGER},",
            "#{dataModel.subjectRoomName,mode=IN,jdbcType=VARCHAR},",
            "#{dataModel.schoolName,mode=IN,jdbcType=VARCHAR},",
            "#{dataModel.maxRegisterNumber,mode=IN,jdbcType=INTEGER},",
            "#{dataModel.realRegisterNumber,mode=IN,jdbcType=INTEGER},",
            "#{dataModel.attendNumber,mode=IN,jdbcType=INTEGER},",
            "#{dataModel.statusFlag,mode=IN,jdbcType=INTEGER})"
    })
    @Options(statementType = StatementType.CALLABLE)
    int save(@Param("dataModel") SubjectRoom dataModel);

    /// <summary>
    /// 根据主键获取一条SubjectRoom表数据
    /// </summary>
    /// <param name="subjectRoomId">教室id</param>
    /// <returns>查询到的表实体对象</returns>
    @Select("call VideoClassMain_SP_SubjectRoom_ReadByKey(#{subjectRoomId})")
    @Options(statementType = StatementType.CALLABLE)
    SubjectRoom getByKey(@Param("subjectRoomId") int subjectRoomId);

    /// <summary>
    /// 获取课程下的教室信息列表
    /// </summary>
    /// <param name="subjectId"></param>
    /// <returns></returns>
    @Select("call VideoClassMain_SP_SubjectRoom_GetListBySubjectId(#{subjectId})")
    @Options(statementType = StatementType.CALLABLE)
    List<SubjectRoomTeacherNameView> getSubjectRoomList(@Param("subjectId") int subjectId);

    /// <summary>
    /// 获取某学生已报名某课程的教室
    /// </summary>
    /// <param name="subjectId">课程id</param>
    /// <param name="studentId">学生id</param>
    /// <returns>查询到的表实体对象</returns>
    @Select("call VideoClassMain_SP_SubjectRoom_ReadByStudentId(#{subjectId},#{studentId})")
    @Options(statementType = StatementType.CALLABLE)
    SubjectRoom getSubjectRoomByStudentId(@Param("subjectId") int subjectId, @Param("studentId") int studentId);

    /// <summary>
    /// 获取某老师已旁听某课程的教室
    /// </summary>
    /// <param name="subjectId">课程id</param>
    /// <param name="studentId">学生id</param>
    /// <returns>查询到的表实体对象</returns>
    @Select("SELECT SubjectRoomID,SubjectID,TeacherID,SubjectRoomName,SchoolName,MaxRegisterNumber,RealRegisterNumber,AttendNumber,StatusFlag,CreateDateTime,UpdateDateTime FROM SubjectRoom WHERE SubjectID=#{subjectId}")
    List<SubjectRoom> getSubjectRoomBySubjectId(@Param("subjectId") int subjectId);

    /// <summary>
    /// 获取某老师已旁听某课程的教室
    /// </summary>
    /// <param name="subjectId">课程id</param>
    /// <param name="studentId">学生id</param>
    /// <returns>查询到的表实体对象</returns>
    @Select("call VideoClassMain_SP_SubjectRoom_ReadByTeacherId(#{subjectId},#{teacherId})")
    @Options(statementType = StatementType.CALLABLE)
    SubjectRoom getSubjectRoomByTeacherId(@Param("subjectId") int subjectId, @Param("teacherId") int teacherId);

    /// <summary>
    /// 刷新实际报名人数
    /// </summary>
    /// <param name="subjectRoomId"></param>
    @Select("call VideoClassMain_SP_SubjectRoom_RefreshRegisterNumber(#{subjectRoomId}})")
    @Options(statementType = StatementType.CALLABLE)
    void refreshRegisterNumber(@Param("subjectRoomId") int subjectRoomId);

    /// <summary>
    /// 删除教室
    /// </summary>
    /// <param name="classRoomIds"></param>
    @Select("call VideoClassMain_SP_SubjectRoom_ReadByTeacherId(#{subjectRoomIds})")
    @Options(statementType = StatementType.CALLABLE)
    void deleteRooms(@Param("subjectRoomIds") String subjectRoomIds);

}
