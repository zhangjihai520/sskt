package com.ry.sskt.model.common.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.ry.sskt.core.annotation.IRedisStoredObject;
import io.protostuff.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.var;

/**
 * <p>
 * 年级表，七年级，八年级，九年级，高一，高二，高三
 * </p>
 *
 * @author xrq
 * @since 2020-04-17
 */
@Data
@Accessors(chain = true)
public class Grade {

    private static final long serialVersionUID = 1L;

    /**
     * 年级ID
     */
    @JSONField(name = "GradeId")
    private int gradeId;

    /**
     * 学科类别
     */
    @JSONField(name = "CourseTypeId")
    private String gradeName;

    /**
     * 年级名称
     */
    @JSONField(name = "GradeName")
    private int courseTypeId;

    /**
     * 计算学级的时候需要减去的偏移量 ，从0开始
     */
    @JSONField(name = "StudyYearOffSet")

    private int studyYearOffSet;

    /// <summary>
    /// 根据年级Id获取题目来源（试卷名）中的年级的别名关键字
    /// </summary>
    /// <param name="gradeId">年级Id</param>
    /// <returns>年级Id对应的别名</returns>
    public static List<String> GetGradeExamAlias(int gradeId) {
        var result = new LinkedList<String>();
        switch (gradeId) {
            case 1:
                result.add("七年级");
                result.add("初一");
                result.add("7年级");
                break;
            case 2:
                result.add("八年级");
                result.add("初二");
                result.add("8年级");
                break;
            case 3:
                result.add("九年级");
                result.add("初三");
                result.add("9年级");
                result.add("中考");
                result.add("模拟");
                result.add("升学");
                result.add("毕业");
                result.add("一模");
                result.add("二模");
                result.add("三模");
                break;
            case 4:
                result.add("高一");
                break;
            case 5:
                result.add("高二");
                break;
            case 6:
                result.add("高三");
                result.add("高考");
                result.add("模拟");
                result.add("一模");
                result.add("二模");
                result.add("三模");
                break;
            case 7:
                result.add("一年级");
                result.add("1年级");
                break;
            case 8:
                result.add("二年级");
                result.add("2年级");
                break;
            case 9:
                result.add("三年级");
                result.add("3年级");
                break;
            case 10:
                result.add("四年级");
                result.add("4年级");
                break;
            case 11:
                result.add("五年级");
                result.add("5年级");
                break;
            case 12:
                result.add("六年级");
                result.add("6年级");
                result.add("模拟");
                result.add("升学");
                result.add("毕业");
                result.add("小升初");
                break;
        }
        return result;
    }

    /// <summary>
    /// 根据学期类型获取上下学期的别名集合
    /// </summary>
    /// <param name="semesterType">学期类型：1 上学期；2 下学期</param>
    /// <returns>学期别名</returns>
    public static List<String> GetGradeSemesterTypeAlias(int semesterType) {
        var result = new LinkedList<String>();
        switch (semesterType) {
            case 1:
                result.add("（上）");
                result.add("(上)");
                result.add("上期");
                result.add("上学期");
                break;
            case 2:
                result.add("（下）");
                result.add("(下)");
                result.add("下期");
                result.add("下学期");
                break;

        }
        return result;
    }
}
