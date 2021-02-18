package com.ry.sskt.core.dledc.utils;

import com.ry.sskt.core.dledc.response.ClassInfo;
import com.ry.sskt.model.common.constant.UserRoleEnum;
import com.ry.sskt.model.common.constant.UserTypeEnum;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;

/// <summary>
/// 平台参数转换帮助类
/// </summary>
public class DledcConvertHelper {
    /// <summary>
    /// 通过班级获取年级id
    /// </summary>
    /// <param name="departmentId"></param>
    /// <param name="classList"></param>
    /// <returns></returns>
    public static String GetGradeCode(String departmentId, List<ClassInfo> classList) {
        if (CollectionUtils.isEmpty(classList)) {
            return StringUtils.EMPTY;
        }
        ClassInfo classInfo = classList.stream().filter(x -> x.getDepartmentId().equals(departmentId)).findFirst().orElse(null);
        if (classInfo != null) {
            return classInfo.getGradeCode();
        }
        return null;
    }

    /// <summary>
    /// 转换年级id
    /// </summary>
    /// <param name="gradeCode"></param>
    /// <returns></returns>
    public static int ConvertGradeId(String gradeCode) {
        if (StringUtils.isBlank(gradeCode)) {
            return 0;
        }
        int sourceGradeId = Integer.parseInt(gradeCode);
        int result = 0;
        switch (sourceGradeId) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                result = sourceGradeId + 6;
                break;
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
                result = sourceGradeId - 6;
                break;
        }
        return result;
    }

    /// <summary>
    /// 转换用户类型
    /// </summary>
    /// <param name="userType"></param>
    /// <returns></returns>
    public static int ConvertUserType(String userType) {
        if (StringUtils.isBlank(userType)) {
            return 0;
        }
        int result = 0;
        switch (userType) {
            case "teacher":
                result = (int) UserTypeEnum.Teacher.getCode();
                break;
            case "student":
                result = (int) UserTypeEnum.Student.getCode();
                break;
            case "parent":
                result = (int) UserTypeEnum.Parent.getCode();
                break;
            case "org":
                result = (int) UserTypeEnum.Agent.getCode();
                break;
        }
        return result;
    }

    /// <summary>
    /// 批量转换用户角色
    /// </summary>
    /// <param name="sourseUserRoles"></param>
    /// <returns></returns>
    public static String ConvertUserRoles(List<String> sourseUserRoles) {
        if (CollectionUtils.isEmpty(sourseUserRoles)) {
            return StringUtils.EMPTY;
        }
        List<Integer> userRoles = new LinkedList<Integer>();
        for (String item : sourseUserRoles) {
            int userRole = ConvertUserRole(item);
            if (userRole != 0) {
                userRoles.add(userRole);
            }
        }
        return StringUtils.join(userRoles, ",");
    }

    /// <summary>
    /// 转换用户角色
    /// </summary>
    /// <param name="sourseUserRole"></param>
    /// <returns></returns>
    public static int ConvertUserRole(String sourseUserRole) {
        if (StringUtils.isBlank(sourseUserRole)) {
            return 0;
        }
        int result = 0;
        switch (sourseUserRole) {
            case "teacher":
                result = UserRoleEnum.Teacher.getCode();
                break;
            case "student":
                result = UserRoleEnum.Student.getCode();
                break;
            case "parent":
                result = UserRoleEnum.Parent.getCode();
                break;
            case "schoolmanager":
                result = UserRoleEnum.SchoolManager.getCode();
                break;
        }
        return result;
    }
}
