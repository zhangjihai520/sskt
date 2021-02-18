
package com.ry.sskt.core.utils;


import com.ry.sskt.model.common.constant.CommonConst;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/* *
 *类名：DateUtil
 *功能：自定义订单类
 *详细：工具类，可以用作获取系统日期、订单编号等
 *版本：3.3
 *日期：2012-08-17
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */
public class DateUtil {

    /**
     * 年月日时分秒(无下划线) yyyyMMddHHmmss
     */
    public static final String dtLong = "yyyyMMddHHmmss";

    /**
     * 完整时间 yyyy-MM-dd HH:mm:ss
     */
    public static final String simple = "yyyy-MM-dd HH:mm:ss";

    /**
     * 完整时间 yyyy-MM-dd HH:mm:ss
     */
    public static final String ymdShort = "yyyy-MM-dd";

    /**
     * 年月日(无下划线) yyyyMMdd
     */
    public static final String dtShort = "yyyyMMdd";
    /**
     * 年-月
     */
    public static final String ymShort = "yyyy-MM";

    /**
     * 年月日时分秒(无下划线) yyyyMMddHHmmss
     */
    public static final String ssLong = "mmss";

    public static LocalDate StringTOLocalDate(String dateStr, String fm) throws ParseException {
        Date dates = new SimpleDateFormat(fm).parse(dateStr);
        Instant instant = dates.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDate();
    }

    public static LocalDateTime StringTOLocalDateTime(String dateStr, String fm) throws ParseException {
        Date dates = new SimpleDateFormat(fm).parse(dateStr);
        Instant instant = dates.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDateTime();
    }

    /**
     * 返回系统当前时间(精确到毫秒),作为一个唯一的订单编号
     *
     * @return 以yyyyMMddHHmmss为格式的当前系统时间
     */
    public static String getOrderNum() {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat(dtLong);
        return df.format(date);
    }

    /**
     * 获取系统当前日期(精确到毫秒)，格式：yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getDateFormatter() {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat(simple);
        return df.format(date);
    }

    /**
     * 获取系统当期年月日(精确到天)，格式：yyyyMMdd
     *
     * @return
     */
    public static String getDate() {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat(dtShort);
        return df.format(date);
    }

    /**
     * 根据毫秒转换成秒
     *
     * @return
     */
    public static int convertMillToSecond(long millSecond) {
        return (int) (millSecond / 1000);
    }

    /**
     * 产生随机的三位数
     *
     * @return
     */
    public static String getThree() {
        Random rad = new Random();
        return rad.nextInt(1000) + "";
    }

    /**
     * @Title: diffGetDateStr
     * @Description: 和当前时间对比（返回值毫秒）
     * @Param @param dateStr
     * @Param @return
     * @Return long
     * @Throws
     */
    public static long diffGetDateStr(String dateStr) {
        DateFormat df = new SimpleDateFormat(simple);
        try {
            Date d1 = new Date();
            Date d2 = df.parse(dateStr);
            long diff = d1.getTime() - d2.getTime();
            return diff;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 返回系统当前时间(精确到毫秒),作为一个唯一的订单编号
     *
     * @return mmss为格式的当前系统时间
     */
    public static String getSecond() {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat(ssLong);
        return df.format(date);
    }

    /**
     * 生成随机时间
     *
     * @return
     */
    public static Date randomDate(String beginDate, String endDate) {

        try {

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            Date start = format.parse(beginDate);//构造开始日期 

            Date end = format.parse(endDate);//构造结束日期 

            //getTime()表示返回自 1970 年 1 月 1 日 00:00:00 GMT 以来此 Date 对象表示的毫秒数。 

            if (start.getTime() >= end.getTime()) {

                return null;

            }

            long date = random(start.getTime(), end.getTime());

            return new Date(date);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }

    private static long random(long begin, long end) {

        long rtn = begin + (long) (Math.random() * (end - begin));

        //如果返回的是开始时间和结束时间，则递归调用本函数查找随机值 

        if (rtn == begin || rtn == end) {

            return random(begin, end);

        }

        return rtn;

    }

    /**
     * 把符合日期格式的字符串转换为日期类型
     */
    public static Date stringtoDate(String dateStr, String format) {
        Date d = null;
        SimpleDateFormat formater = new SimpleDateFormat(format);
        try {
            formater.setLenient(false);
            d = formater.parse(dateStr);
        } catch (Exception e) {
            // log.error(e);
            d = null;
        }
        return d;
    }

    /**
     * 把日期转换为字符串
     */
    public static String dateToString(Date date, String format) {
        String result = "";
        SimpleDateFormat formater = new SimpleDateFormat(format);
        try {
            result = formater.format(date);
        } catch (Exception e) {
            // log.error(e);
        }
        return result;
    }

    /**
     * @param date, f
     * @return java.lang.String
     * @Description 日期转换为字符串 格式自定义
     * @Author NieHui
     * @Date 2019/4/27 9:41
     */
    public static String dateStr(Date date, String f) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(f);
        String str = format.format(date);
        return str;
    }

    /**
     * @param date1, date2
     * @return int
     * @Description 计算两个日期之间的秒数
     * @Author NieHui
     * @Date 2019/4/27 9:35
     */
    public static String secondBetweenByDateStr(Date date1, Date date2) {
        DateFormat sdf = new SimpleDateFormat(dtLong);
        Calendar cal = Calendar.getInstance();
        try {
            Date d1 = sdf.parse(DateUtil.dateStr(date1, dtLong));
            Date d2 = sdf.parse(DateUtil.dateStr(date2, dtLong));
            cal.setTime(d1);
            long time1 = cal.getTimeInMillis();
            cal.setTime(d2);
            long time2 = cal.getTimeInMillis();
            return String.valueOf(((time2 - time1) / 1000L));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    //判断选择的日期是否是本周
    public static boolean isThisWeek(long time) {
        Calendar calendar = Calendar.getInstance();
        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        calendar.setTime(new Date(time));
        int paramWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        if (paramWeek == currentWeek) {
            return true;
        }
        return false;
    }

    //判断选择的日期是否是今天
    public static boolean isToday(long time) {
        return isThisTime(time, "yyyy-MM-dd");
    }

    //判断选择的日期是否是本月
    public static boolean isThisMonth(long time) {
        return isThisTime(time, "yyyy-MM");
    }

    private static boolean isThisTime(long time, String pattern) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String param = sdf.format(date);//参数时间
        String now = sdf.format(new Date());//当前时间
        if (param.equals(now)) {
            return true;
        }
        return false;
    }

    public static Date ChineseCharToDate(String chineseChar) {
        String dateStr = ChineseCharToDateStr(chineseChar);
        return DateUtil.stringtoDate(dateStr, "yyyy-MM");
    }

    public static String ChineseCharToDateStr(String chineseChar) {
        String dateStr = chineseChar.replace("年", "-").replace("月", "-").replace("日", "-");
        return dateStr.substring(0, dateStr.length() - 1);
    }

    /**
     * 获取上一个月的
     *
     * @param date
     * @return
     */
    public static String upMothStr(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -1);
        Date m = c.getTime();
        return format.format(m);
    }

    /**
     * @return int
     * @Description 计算年龄
     * @Author jx
     * @Date 2019/7/16 18:40
     * @Param [birthDay
     */
    public static int getAgeByBirth(Date birthDay) {
        int age = 0;
        Calendar cal = Calendar.getInstance();
        //出生日期晚于当前时间，无法计算
        if (cal.before(birthDay)) {
            return 0;
        }
        int yearNow = cal.get(Calendar.YEAR);  //当前年份
        int monthNow = cal.get(Calendar.MONTH);  //当前月份
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH); //当前日期
        cal.setTime(birthDay);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        //计算整岁数
        age = yearNow - yearBirth;
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                //当前日期在生日之前，年龄减一
                if (dayOfMonthNow < dayOfMonthBirth) age--;
            } else {
                //当前月份在生日之前，年龄减一
                age--;
            }
        }
        return age;
    }


    /**
     * 比较相差的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDaysByDate(Date date1, Date date2) {
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));
        return days;
    }


    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }


    public static Date dateCount(Date startDate, int feeCycleNum, int feeCycleUnit) {

        //使用默认时区和语言环境获得一个日历。
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        if (feeCycleUnit == CommonConst.YEAR) {
            cal.add(Calendar.YEAR, +feeCycleNum);
        }

        if (feeCycleUnit == CommonConst.MONTH) {
            cal.add(Calendar.MONTH, +feeCycleNum);
        }

        if (feeCycleUnit == CommonConst.DAY) {
            cal.add(Calendar.DAY_OF_MONTH, +feeCycleNum);
        }

        return cal.getTime();
    }

    /**
     * 获得指定日期的前一天
     *
     * @param specifiedDay
     * @return
     * @throws Exception
     */
    public static String getSpecifiedDayBefore(String specifiedDay) {//可以用new Date().toLocalString()传递参数
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
        } catch (Exception e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);

        String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return dayBefore;
    }


    public static LocalDate LocalDateMIN() {
        return LocalDate.parse("1970-01-01", CommonConst.Datefomat_yyyyMMdd);
    }

    public static LocalDate LocalDateMAX() {
        return LocalDate.parse("2999-12-31", CommonConst.Datefomat_yyyyMMdd);
    }

    public static LocalDateTime LocalDateTimeMIN() {
        return LocalDateTime.parse("1970-01-01 00:00:00", CommonConst.datefomat_nomal);
    }

    public static LocalDateTime LocalDateTimeMAX() {
        return LocalDateTime.parse("2999-12-31 23:59:59", CommonConst.datefomat_nomal);
    }

}
