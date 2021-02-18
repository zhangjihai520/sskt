package com.ry.sskt.core.utils;

import com.ry.sskt.model.common.constant.CommonConst;
import com.ry.sskt.model.common.constant.SubjectClassStateEnum;
import com.ry.sskt.model.common.constant.SubjectStatusEnum;
import com.ry.sskt.model.common.constant.SubjectStatusFlagEnum;
import com.ry.sskt.model.common.entity.FourthTuple;
import com.ry.sskt.model.subject.entity.Subject;
import com.ry.sskt.model.subject.entity.view.SubjectListByFilterView;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Component
@Slf4j
public class CommonUtil {
    private static long sDayStart;
    private static String sDateString;
    private static SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHH");
    private static String fileNameFormat = "%s（%s—%s）";

    public static String genFileName(String beginTime, String endTime, String name) {
        return String.format(fileNameFormat, name, beginTime, endTime);
    }




    /// <summary>
    /// 根据课程信息获取SubjectStatusEnum
    /// </summary>
    /// <param name="mySubject"></param>
    /// <returns></returns>
    public static SubjectStatusEnum GetSubjectStatusEnum(Subject mySubject) {
        var now = LocalDateTime.now();
        if (mySubject.getStatusFlag() == 1) {
            return SubjectStatusEnum.Invalid;
        }
        if (mySubject.getClassStateId() == 2) {
            return SubjectStatusEnum.InClass;
        }
        if (mySubject.getClassStateId() == 3 || mySubject.getClassStateId() == 4) {
            return SubjectStatusEnum.ClosedClass;
        }
        if (now.isAfter(mySubject.getBeginTime()) && now.isBefore(mySubject.getEndTime())) {
            return SubjectStatusEnum.InClass;
        }
        if (mySubject.getEndTime().isAfter(mySubject.getBeginTime()) && now.isAfter(mySubject.getEndTime())) {
            return SubjectStatusEnum.ClosedClass;
        }
        if (now.isBefore(mySubject.getRegistBeginTime())) {
            return SubjectStatusEnum.ToBeRegistered;
        }
        if ((mySubject.getRegistBeginTime().isBefore(now) || mySubject.getRegistBeginTime().isEqual(now)) && now.isBefore(mySubject.getRegistEndTime())) {
            return SubjectStatusEnum.Registration;
        }
        if ((mySubject.getRegistEndTime().isBefore(now) || mySubject.getRegistEndTime().isEqual(now)) && now.isBefore(mySubject.getBeginTime().plusMinutes(60))) {
            return SubjectStatusEnum.WaitingForClass;
        }
        return SubjectStatusEnum.ClosedClass;
    }

    /// <summary>
    /// 根据课程信息获取SubjectStatusEnum
    /// </summary>
    /// <param name="mySubject"></param>
    /// <returns></returns>
    public static SubjectStatusEnum GetSubjectStatusEnum(SubjectListByFilterView mySubject) {
        var now = LocalDateTime.now();
        if (mySubject.getStatusFlag() == 1) {
            return SubjectStatusEnum.Invalid;
        }
        if (mySubject.getClassStateId() == 2) {
            return SubjectStatusEnum.InClass;
        }
        if (mySubject.getClassStateId() == 3 || mySubject.getClassStateId() == 4) {
            return SubjectStatusEnum.ClosedClass;
        }

        if (now.isAfter(mySubject.getBeginTime()) && now.isBefore(mySubject.getEndTime())) {
            return SubjectStatusEnum.InClass;
        }

        if (mySubject.getEndTime().isAfter(mySubject.getBeginTime()) && now.isAfter(mySubject.getEndTime())) {
            return SubjectStatusEnum.ClosedClass;
        }


        if (now.isBefore(mySubject.getRegistBeginTime())) {
            return SubjectStatusEnum.ToBeRegistered;
        }
        if ((mySubject.getRegistBeginTime().isBefore(now) || mySubject.getRegistBeginTime().isEqual(now)) && now.isBefore(mySubject.getRegistEndTime())) {
            return SubjectStatusEnum.Registration;
        }
        if ((mySubject.getRegistEndTime().isBefore(now) || mySubject.getRegistEndTime().isEqual(now)) && now.isBefore(mySubject.getBeginTime().plusMinutes(60))) {
            return SubjectStatusEnum.WaitingForClass;
        }
        return SubjectStatusEnum.ClosedClass;
    }

    /// <summary>
    /// 根据课程信息获取SubjectStatusEnum
    /// </summary>
    /// <param name="myEnum"></param>
    /// <returns>第一个为statusFlag，第二个为v_MaxTimeField，第三个为v_MinTimeField，第四个为班级上课状态</returns>
    public static FourthTuple<SubjectStatusFlagEnum, String, String, SubjectClassStateEnum> GetFieldNames(int myEnum) {
        if (myEnum == SubjectStatusEnum.All.getIndex())
            return new FourthTuple<SubjectStatusFlagEnum, String, String, SubjectClassStateEnum>(SubjectStatusFlagEnum.Delete, StringUtils.EMPTY, StringUtils.EMPTY, SubjectClassStateEnum.All);
        else if (myEnum == SubjectStatusEnum.Invalid.getIndex())
            return new FourthTuple<SubjectStatusFlagEnum, String, String, SubjectClassStateEnum>(SubjectStatusFlagEnum.Invalid, StringUtils.EMPTY, StringUtils.EMPTY, SubjectClassStateEnum.All);
        else if (myEnum == SubjectStatusEnum.ToBeRegistered.getIndex())
            return new FourthTuple<SubjectStatusFlagEnum, String, String, SubjectClassStateEnum>(SubjectStatusFlagEnum.ToBeRegistered, "RegistBeginTime", StringUtils.EMPTY, SubjectClassStateEnum.All);
        else if (myEnum == SubjectStatusEnum.Registration.getIndex())
            return new FourthTuple<SubjectStatusFlagEnum, String, String, SubjectClassStateEnum>(SubjectStatusFlagEnum.ToBeRegistered, "RegistEndTime", "RegistBeginTime", SubjectClassStateEnum.All);
        else if (myEnum == SubjectStatusEnum.WaitingForClass.getIndex())
            return new FourthTuple<SubjectStatusFlagEnum, String, String, SubjectClassStateEnum>(SubjectStatusFlagEnum.ToBeRegistered, "ADDDATE(BeginTime,INTERVAL 1 HOUR)", "RegistEndTime", SubjectClassStateEnum.All);
        else if (myEnum == SubjectStatusEnum.ClosedClass.getIndex())
            return new FourthTuple<SubjectStatusFlagEnum, String, String, SubjectClassStateEnum>(SubjectStatusFlagEnum.ToBeRegistered, StringUtils.EMPTY, "EndTime", SubjectClassStateEnum.ClosedClass);
        else if (myEnum == SubjectStatusEnum.InClass.getIndex())
            return new FourthTuple<SubjectStatusFlagEnum, String, String, SubjectClassStateEnum>(SubjectStatusFlagEnum.ToBeRegistered, StringUtils.EMPTY, StringUtils.EMPTY, SubjectClassStateEnum.InClass);
        else
            throw new NotImplementedException("SubjectStatusEnum未实现转换");
    }


    /// <summary>
    /// 计算文件大小函数(保留两位小数),Size为字节大小
    /// </summary>
    /// <param name="size">初始文件大小</param>
    /// <returns></returns>
    public static String getFileSize(long size) {
        double num = 1024.00; //byte
        if (size < num) {
            if (size / num > 0.9)
                return String.format("%.1f", (size / num)) + "K"; //kb
            return size + "B";
        }

        if (size < Math.pow(num, 2)) {
            if (size / Math.pow(num, 2) > 0.9)
                return String.format("%.1f", (size / Math.pow(num, 2))) + "M"; //M
            return String.format("%.1f", (size / num)) + "K"; //kb
        }

        if (size < Math.pow(num, 3)) {
            if (size / Math.pow(num, 3) > 0.9)
                return String.format("%.1f", (size / Math.pow(num, 3))) + "G"; //M
            return String.format("%.1f", (size / Math.pow(num, 2))) + "M"; //M
        }

        if (size < Math.pow(num, 4)) {
            if (size / Math.pow(num, 4) > 0.9)
                return String.format("%.1f", (size / Math.pow(num, 4))) + "T"; //M
            return String.format("%.1f", (size / Math.pow(num, 3))) + "G"; //G
        }

        return String.format("%.1f", (size / Math.pow(num, 4))) + "T"; //T
    }

    public static <T> List<T> castList(Object obj, Class<T> clazz) {
        List<T> result = new LinkedList<T>();
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }

    public static int getAgeByFactoryDate(Date birthDay) {
        int age = 0;
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) { //出生日期晚于当前时间，无法计算
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);  //当前年份
        int monthNow = cal.get(Calendar.MONTH);  //当前月份
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH); //当前日期
        cal.setTime(birthDay);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        age = yearNow - yearBirth;   //计算整岁数
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;//当前日期在生日之前，年龄减一
            } else {
                age--;//当前月份在生日之前，年龄减一
            }
        }
        return age;
    }

    //随机字符串生成
    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 获取所有校验错误信息
     *
     * @param allErrors
     * @return
     */
    public static String getAllValidateError(List<ObjectError> allErrors) {
        List<String> errorStrs = new LinkedList<String>();
        for (ObjectError error : allErrors) {
            errorStrs.add(error.getDefaultMessage());
        }
        return String.join(",", errorStrs);
    }

    /**
     * 获取重复提交key-->duplicate_token_key+','+请求方法名
     *
     * @param methodName
     * @param sessionId
     * @return
     */
    public static String getDuplicateTokenKey(String methodName, String sessionId) {
        StringBuilder key = new StringBuilder(CommonConst.DUPLICATE_TOKEN_KEY);
        key.append(methodName).append(sessionId);
        return key.toString();
    }


    private static Date getStartOfDay() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        calendar.set(year, month, day, hourOfDay, 0, 0);
        return calendar.getTime();
    }

    /**
     * 分转元
     *
     * @param price
     * @return
     */
    public static BigDecimal changeF2Y(BigDecimal price) {
        BigDecimal decimal;
        if (null != price) {
            decimal = price.divide(new BigDecimal(100),0, RoundingMode.HALF_UP);
        } else {
            decimal = new BigDecimal("0.00");
        }
        return decimal.setScale(2);
    }

    /**
     * 元转分，确保price保留两位有效数字
     *
     * @return
     */
    public static long changeY2F(BigDecimal price) {
        if (null != price) {
            price.setScale(2);
            long money = (long) (price.doubleValue() * 100);
            return money;
        } else {
            return 0;
        }

    }

    /**
     * 元转换成分
     *
     * @param amount
     * @return
     */
    public static String getMoney(String amount) {
        if (amount == null) {
            return "";
        }
        // 金额转化为分为单位
        String currency = amount.replaceAll("\\$|\\￥|\\,", "");  //处理包含, ￥ 或者$的金额
        int index = currency.indexOf(".");
        int length = currency.length();
        Long amLong = 0l;
        if (index == -1) {
            amLong = Long.valueOf(currency + "00");
        } else if (length - index >= 3) {
            amLong = Long.valueOf((currency.substring(0, index + 3)).replace(".", ""));
        } else if (length - index == 2) {
            amLong = Long.valueOf((currency.substring(0, index + 2)).replace(".", "") + 0);
        } else {
            amLong = Long.valueOf((currency.substring(0, index + 1)).replace(".", "") + "00");
        }
        return amLong.toString();
    }


    public static String convertServiceTypeIdsByExtractConfig(String extractConfig) {
        if (null == extractConfig) {
            return null;
        }
        String serviceTypeIds = "";
        String[] arr = extractConfig.split("\\|");
        if (null != arr && arr.length != 0) {
            for (String extract : arr) {
                String[] split = extract.split(":");
                serviceTypeIds = serviceTypeIds + split[0] + ",";

            }
        }

        return serviceTypeIds.substring(0, serviceTypeIds.length() - 1);
    }

    public static List<String> imgsConvertList(String imgs) {
        //判断是否有图片保存
        if (StringUtils.isNotBlank(imgs)) {
            String[] imgArrary = imgs.split("\\|");
            List<String> imgList = new LinkedList<>();
            for (int i = 0; i < imgArrary.length; i++) {
                imgList.add(imgArrary[i]);
            }
            return imgList;
        }
        return null;
    }

    public static String imgsConvertString(List<String> imgList) {
        if (CollectionUtils.isNotEmpty(imgList)) {
            StringBuffer buf = new StringBuffer();
            for (String img : imgList) {
                buf.append(img + "|");
            }
            String imgs = buf.toString().substring(0, buf.length() - 1);
            return imgs;
        }
        return null;
    }

    public static String getRandomNumber() {
        int random = (int) ((Math.random() * 9 + 1) * 1000);
        long l = System.currentTimeMillis();
        return String.valueOf(random) + l;
    }

}
