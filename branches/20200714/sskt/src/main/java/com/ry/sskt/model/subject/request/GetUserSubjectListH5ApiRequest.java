package com.ry.sskt.model.subject.request;

import com.ry.sskt.model.common.request.RequestBase;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 获取时间段内的学生开课情况 输入参数
 */
@Data
@Accessors(chain = true)
public class GetUserSubjectListH5ApiRequest extends RequestBase {
    /// <summary>
    /// 获取数据类型 0我的数据  1全部数据
    /// </summary>
    private int TypeEnum;

    /// <summary>
    /// 所选月份，不传返回系统当前月的全部课程
    /// </summary>
    private String SelectedMonth;

    /// <summary>
    /// 所选日期份，可不传 传了后取日期数据与月份数据做合并
    /// </summary>
    private String SelectedDate;

    /// <summary>
    /// 课程类型
    /// </summary>
    private int SubjectGenreId;
    public String getSelectedMonth() {
        if (SelectedMonth.length() < 7) {
            StringBuilder sb = new StringBuilder(SelectedMonth);
            sb.insert(5, 0);
            return sb.toString();
        } else {
            return this.SelectedMonth;
        }
    }
}
