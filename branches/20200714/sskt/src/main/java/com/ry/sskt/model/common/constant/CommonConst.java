package com.ry.sskt.model.common.constant;

import com.ry.sskt.core.config.CommonConfig;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: 幸仁强
 * @Date: 2020/4/16 15:34
 * @Description: 公共常量类
 */
@Component
public class CommonConst {
    public static final String UidKey = "RuanYun_UserIdKey";
    public static final String IDDESKEY = "lQa9_&skzly%!9fs2@*UNA($ck_^:)'aI9e.^2Lbx9,5lf!j+~Hz@^hakuJ^crOb";
    public static final String DUPLICATE_TOKEN_KEY = "duplicate_post:cache";
    public static final int MINUTE_10 = 60 * 10;
    public static final int MINUTE_30 = 60 * 30;
    public static final int MINUTE_60 = 60 * 60;
    public static final int MINUTE_120 = 60 * 60 * 2;
    public static final int MINUTE_360 = 60 * 60 * 6;
    public static final int MINUTE_1440 = 60 * 60 * 24;
    public static final int MINUTE_480 = 60 * 60 * 8;
    public static final int HALF_YEAR = 60 * 60 * 24 * 180;
    public static final String AVCON_PUSH_PORT_1935 = "1935";
    public static final String AVCON_PULL_PORT_1936 = "1936";
    public static final String AVCON_HLS_PROT_8299 = "8299";

    public static final String PINGOS_PUSH_PORT_1937 = "1937";
    public static final String PINGOS_PULL_1938 = "1938";
    public static final String PINGOS_HLS_PROT_8999 = "8999";
    public static final String PINGOS_RECORD_PORT_8899 = "8899";
    public static final String HLS_APP = "hls";
    public static final String LIVE_APP = "live";
    public static final String PINGOS_HLS_STUFF = ".m3u8";
    public static final String DOMAIN = "sskt.nceduc.cn";

    public static final DateTimeFormatter datefomat_nomal = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final DateTimeFormatter datefomat_nomal2 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public static final DateTimeFormatter Datefomat_yyyyMMddHHmm = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

    public static final DateTimeFormatter Datefomat_yyyyMMdd = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static final DateTimeFormatter Datefomat_yyyyMMdd2 = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    /// <summary>
    /// 公益课名称与类型对应，其他均为99
    /// </summary>
    public final static Map<String, PublicBenefitUrlTypeEnum> PublicBenefitCourseNameAndType = new HashMap() {
        {
            put("体育锻炼", PublicBenefitUrlTypeEnum.NewPageRedirect);
            put("红色文化教育", PublicBenefitUrlTypeEnum.NewPageRedirect);
            put("艺术欣赏", PublicBenefitUrlTypeEnum.NewPageRedirect);
            put("健康教育", PublicBenefitUrlTypeEnum.NewPageRedirect);
            put("上课前", PublicBenefitUrlTypeEnum.NewPageRedirect);
            put("模拟练习", PublicBenefitUrlTypeEnum.OtherPageRedirect);
        }
    };


    public static final String OOS_File_Root_Dir_TalkUpdateFile = "TalkUpdateFile";

    public static final String OOS_File_Root_Dir_Courseware = "Courseware";
    /// <summary>
    /// 文件最大长度(长度字节)
    /// </summary>
    public static final long FileMaxLength = 104857600;
    // 年月日JGPRD
    public static final int YEAR = 3;
    public static final int MONTH = 2;
    public static final int DAY = 1;
}
