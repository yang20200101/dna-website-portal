package com.highershine.portal.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 时间工具类
 */
public class DateTools {

    public static final Logger LOG = LoggerFactory.getLogger(DateTools.class);

    /**
     * 日期格式 yyyy-MM-dd
     */
    public static final String DF_DATE = "yyyy-MM-dd";
    public static final String DF_TODAY = "yyyyMMdd";
    public static final String MONTH = "yyyy-MM";
    public static final String DF_SHANGHAI = "Asia/Shanghai";
    /**
     * 中文日期格式 yyyy年MM月dd日
     */
    public static final String DF_CN_DATE = "yyyy年MM月dd日";
    /**
     * 分钟格式 yyyy-MM-dd HH:mm
     */
    public static final String DF_MINUTE = "yyyy-MM-dd HH:mm";
    /**
     * 时间格式 yyyy-MM-dd HH:mm:ss
     */
    public static final String DF_TIME = "yyyy-MM-dd HH:mm:ss";
    /**
     * 中文时间格式 yyyy年MM月dd日HH时mm分ss秒
     */
    public static final String DF_CN_TIME = "yyyy年MM月dd日HH时mm分ss秒";
    /**
     * 紧凑时间格式 yyyyMMddHHmmss
     */
    public static final String DF_COMPACT_TIME = "yyyyMMddHHmmss";
    /**
     * 日期格式数组（包含常见的各种日期格式）
     */
    private static final String[] DF_DATES = {"yy-MM-dd", "yy.MM.dd",
            "yy/MM/dd", "yy年MM月dd日", "yy年MM月dd号", "yy、MM、dd", "yy·MM·dd",
            "yy_MM_dd", "yy#MM#dd", DF_DATE, "yyyy.MM.dd", "yyyy/MM/dd",
            DF_CN_DATE, "yyyy年MM月dd号", "yyyy、MM、dd", "yyyy·MM·dd",
            "yyyy_MM_dd", "yyyy#MM#dd"};

    /**
     * 内置起始日期
     */
    private static final String START_DATE = "1900-01-01";

    public static final long ONE_DAY = 86400000L;

    /**
     * 获取现在时间
     *
     * @return
     */
    public static Date getNow() {
        TimeZone tz = TimeZone.getTimeZone(DF_SHANGHAI);
        TimeZone.setDefault(tz);
        return new Date();
    }

    /**
     * 获取当前日期字符串
     *
     * @return yyyy-MM-dd
     */
    public static String getNowDateStr() {
        TimeZone tz = TimeZone.getTimeZone(DF_SHANGHAI);
        TimeZone.setDefault(tz);
        Date curDate = new Date();
        return dateToString(curDate);
    }

    /**
     * 获取当前时间
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getNowTimeStr() {
        TimeZone tz = TimeZone.getTimeZone(DF_SHANGHAI);
        TimeZone.setDefault(tz);
        Date curDate = new Date();
        return dateToString(curDate, DF_TIME);
    }

    /**
     * 设置时间时区
     *
     * @return
     */
    public static Date setTimeZone(Date date) {
        TimeZone tz = TimeZone.getTimeZone(DF_SHANGHAI);
        TimeZone.setDefault(tz);
        return date;
    }

    /**
     * 得到现在小时
     */
    public static String getHour() {
        TimeZone tz = TimeZone.getTimeZone(DF_SHANGHAI);
        TimeZone.setDefault(tz);
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(DF_TIME);
        String dateString = formatter.format(currentTime);
        String hour;
        hour = dateString.substring(11, 13);
        return hour;
    }

    /**
     * 得到现在分钟
     *
     * @return
     */
    public static String getMinute() {
        TimeZone tz = TimeZone.getTimeZone(DF_SHANGHAI);
        TimeZone.setDefault(tz);
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(DF_TIME);
        String dateString = formatter.format(currentTime);
        String min;
        min = dateString.substring(14, 16);
        return min;
    }

    /**
     * 日期型转换为"yyyy-MM-dd"字符串型
     *
     * @param date
     * @return 字符串型时间
     */
    public static String dateToString(Date date) {
        String dateStr = "";
        if (date == null) {
            return dateStr;
        }
        try {
            SimpleDateFormat dateFmt = new SimpleDateFormat(DF_DATE);
            dateStr = dateFmt.format(date);
        } catch (Exception e) {
            LOG.error("日期类型转换为字符串类型时出现错误！", e);
        }
        return dateStr;
    }

    /**
     * 日期型转换为"yyyy-MM-dd"字符串型
     *
     * @param date
     * @return 字符串型时间DF_CN_DATE
     */
    public static String dateToCNString(Date date) {
        String dateStr = "";
        try {
            SimpleDateFormat dateFmt = new SimpleDateFormat(DF_CN_DATE);
            dateStr = dateFmt.format(date);
        } catch (Exception e) {
            LOG.error("DateTools.dateToCNString出现错误！", e);
        }
        return dateStr;
    }

    /**
     * 日期型转换为自定义型字符串型
     *
     * @param date
     * @param fm
     * @return 字符串型时间
     */
    public static String dateToString(Date date, String fm) {
        String dateStr = "";
        try {
            SimpleDateFormat dateFmt = new SimpleDateFormat(fm);
            dateStr = dateFmt.format(date);
        } catch (Exception e) {
            LOG.error("", e);
        }
        return dateStr;
    }

    /**
     * 字符串转换为"yyyy-MM-dd HH:mm:ss"日期型
     *
     * @param strDate
     * @return 日期型时间
     */
    public static Date stringToDate(String strDate) {
        TimeZone tz = TimeZone.getTimeZone(DF_SHANGHAI);
        TimeZone.setDefault(tz);
        Date date = new Date();
        DateFormat df = new SimpleDateFormat(DF_TIME);
        try {
            date = df.parse(strDate);
        } catch (ParseException e) {
            return null;
        }
        return date;
    }

    /**
     * 自定义格式 字符串转换为日期型
     *
     * @param strDate
     * @return 日期型时间
     */
    public static Date stringToDate(String strDate, String fm) {
        TimeZone tz = TimeZone.getTimeZone(DF_SHANGHAI);
        TimeZone.setDefault(tz);
        Date date = new Date();
        DateFormat df = new SimpleDateFormat(fm);
        try {
            date = df.parse(strDate);
        } catch (ParseException e) {
            return null;
        }
        return date;
    }

    /**
     * test - agx - temp
     */

    /**
     * 字符串转换为"yyyy-MM-dd HH:mm"日期型
     *
     * @param strDate
     * @return 日期型时间
     */
    public static Date stringToDateWithMinu(String strDate) {
        Date date = null;
        DateFormat df = new SimpleDateFormat(DF_MINUTE);
        try {
            date = df.parse(strDate);
        } catch (ParseException e) {
            return null;
        }
        return date;
    }

    /**
     * 将字符串按fms中的自定义格式组解析为日期格式
     *
     * @param strDate        要解析为日期型的字符串
     * @param fms            解析参照的格式数组
     * @param needVerifyDate 是否要做日期有效性验证（验证的标准是解析出来的日期大于1900年1月1日，小于当前日期的为有效，否则无效）
     * @param startDate      如果需要验证时，起始时间设置
     * @param endDate        如果需要验证时，终止时间设置
     * @return 解析时会循环自定义格式组，如果其中一个格式可以解析出日期，并且日期符合规范，就使用那个日期；如果格式组中的格式均不能解析出日期，则返回null
     */
    public static Date stringToDate(String strDate, String[] fms, boolean needVerifyDate, Date startDate, Date endDate) {
        if (strDate == null || strDate.equals("")) {
            return null;
        }
        Date date = null;
        if (fms == null || fms.length == 0) {
            fms = DF_DATES;
        }
        for (String fm : fms) {
            Date tempDate = stringToDate(strDate, fm);
            if (tempDate != null
                    && (!needVerifyDate || isDateValid(tempDate, startDate, endDate))) {
                date = tempDate;
                break;
            }
        }
        return date;
    }

    /**
     * 方法重构:将字符串按fms中的自定义格式组解析为日期格式
     *
     * @param strDate        要解析为日期型的字符串
     * @param fms            解析参照的格式数组(如果为空，则按照本工具类自定义的格式数组进行解析)
     * @param needVerifyDate 是否要做日期有效性验证（验证的标准是解析出来的日期大于1900年1月1日，小于当前日期的为有效，否则无效）
     * @return 解析时会循环自定义格式组，如果其中一个格式可以解析出日期，并且日期符合规范，就使用那个日期；如果格式组中的格式均不能解析出日期，则返回null
     */
    public static Date stringToDate(String strDate, String[] fms, boolean needVerifyDate) {
        return stringToDate(strDate, fms, needVerifyDate, null, null);
    }

    /**
     * 字符串为"yyyy-MM-dd HH:mm"转换为时间戳型
     *
     * @param dateString
     * @return 时间戳型
     * @throws ParseException
     */
    public static final Timestamp string2Time(String dateString) throws ParseException {
        DateFormat dateFormat;
        //设定格式
        dateFormat = new SimpleDateFormat(DF_MINUTE, Locale.ENGLISH);
        dateFormat.setLenient(false);
        //util类型
        Date timeDate = dateFormat.parse(dateString);
        return date2TimeStamp(timeDate);
    }

    /**
     * 字符串为"yyyy-MM-dd HH:mm"转换为时间戳型
     *
     * @param date
     * @return 时间戳型
     * @throws ParseException
     */
    public static final Timestamp date2TimeStamp(Date date) {
        //Timestamp类型,timeDate.getTime()返回一个long型
        return new Timestamp(date.getTime());
    }

    /**
     * 验证veryfiedDate日期是否合法 判断依据：veryfiedDate大于1900年1月1日，并且小于当前日期的即为合法，否则不合法
     *
     * @param veryfiedDate
     * @return
     */
    public static boolean isDateValid(Date veryfiedDate) {
        return isDateValid(veryfiedDate, null, null);
    }

    /**
     * 验证veryfiedDate日期是否合法 判断依据：veryfiedDate大于startDate，并且小于endDate的即为合法，否则不合法
     *
     * @param veryfiedDate
     * @param startDate    如果startDate为空，比较的起始时间就设置为1900年1月1日
     * @param endDate      如果endDate为空，比较的终止时间就设置为当前日期
     * @return
     */
    public static boolean isDateValid(Date veryfiedDate, Date startDate, Date endDate) {
        TimeZone tz = TimeZone.getTimeZone(DF_SHANGHAI);
        TimeZone.setDefault(tz);
        boolean result = false;
        if (veryfiedDate != null) {
            if (startDate == null) {
                startDate = stringToDate(START_DATE, DF_DATE);
            }
            if (endDate == null) {
                endDate = new Date();
            }
            if (veryfiedDate.after(startDate) && veryfiedDate.before(endDate)) {
                result = true;
            }
        }
        return result;
    }

    /**
     * 二个小时时间间的差值,必须保证二个时间都是"HH:mm"的格式，返回字符型的分钟
     *
     * @param st1
     * @param st2
     * @return
     */
    public static String getTwoHour(String st1, String st2) {
        String[] kk = null;
        String[] jj = null;
        kk = st1.split(":");
        jj = st2.split(":");
        if (Integer.parseInt(kk[0]) < Integer.parseInt(jj[0])) {
            return "0";
        } else {
            double y = Double.parseDouble(kk[0]) + Double.parseDouble(kk[1])
                    / 60;
            double u = Double.parseDouble(jj[0]) + Double.parseDouble(jj[1])
                    / 60;
            if ((y - u) > 0) {
                return y - u + "";
            } else {
                return "0";
            }
        }
    }

    /**
     * 得到二个日期间的间隔天数
     *
     * @param sj1 yyyy-MM-dd
     * @param sj2 yyyy-MM-dd
     * @return
     */
    public static String getTwoDay(String sj1, String sj2) {
        SimpleDateFormat myFormatter = new SimpleDateFormat(DF_DATE);
        long day = 0;
        try {
            Date date = myFormatter.parse(sj1);
            Date mydate = myFormatter.parse(sj2);
            day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
            return "";
        }
        return day + "";
    }

    /**
     * 时间前推或后推分钟,其中JJ表示分钟.
     *
     * @param sj1 "yyyy-MM-dd HH:mm:ss"
     * @param jj
     * @return
     */
    public static String getPreTime(String sj1, String jj) {
        SimpleDateFormat format = new SimpleDateFormat(DF_TIME);
        String mydate1 = "";
        try {
            Date date1 = format.parse(sj1);
            long time = (date1.getTime() / 1000) + Integer.parseInt(jj) * 60;
            date1.setTime(time * 1000);
            mydate1 = format.format(date1);
        } catch (Exception e) {
            LOG.error("DateTools.getPreTime出现错误！", e);
        }
        return mydate1;
    }

    /**
     * 得到一个时间延后或前移几天的时间
     *
     * @param nowdate 时间 "yyyy-MM-dd"
     * @param delay   为前移或后延的天数
     * @return
     */
    public static String getNextDay(String nowdate, String delay) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(DF_DATE);
            String mdate = "";
            Date d = stringToDate(nowdate, DF_DATE);
            long myTime = 0;
            if (d != null) {
                myTime = (d.getTime() / 1000) + Integer.parseInt(delay) * 24 * 60 * 60;
            } else {
                return "";
            }
            d.setTime(myTime * 1000);
            mdate = format.format(d);
            return mdate;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 判断是否润年
     *
     * @param date
     * @return
     */
    public static boolean isLeapYear(String date) {
        /**
         * 详细设计： 1.被400整除是闰年，否则： 2.不能被4整除则不是闰年 3.能被4整除同时不能被100整除则是闰年
         * 3.能被4整除同时能被100整除则不是闰年
         */
        Date d = stringToDate(date, DF_DATE);
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(d);
        int year = gc.get(Calendar.YEAR);

        boolean flag = false;
        if ((year % 400) == 0) {
            flag = true;
        } else if ((year % 4) == 0) {
            if ((year % 100) == 0) {
                flag = false;
            } else {
                flag = true;
            }
        } else {
            flag = false;
        }
        return flag;
    }

    /**
     * 获取一个月的最后一天
     *
     * @param dat yyyy-MM-dd
     * @return
     */
    public static String getEndDateOfMonth(String dat) {
        String str = dat.substring(0, 8);
        String month = dat.substring(5, 7);
        int mon = Integer.parseInt(month);
        if (mon == 1 || mon == 3 || mon == 5 || mon == 7 || mon == 8 || mon == 10 || mon == 12) {
            str += "31";
        } else if (mon == 4 || mon == 6 || mon == 9 || mon == 11) {
            str += "30";
        } else {
            if (isLeapYear(dat)) {
                str += "29";
            } else {
                str += "28";
            }
        }
        return str;
    }

    /**
     * 判断二个时间是否在同一个周
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameWeekDates(Date date1, Date date2) {
        boolean flag = false;
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
        if ((0 == subYear || (1 == subYear && 11 == cal2.get(Calendar.MONTH))
                || (-1 == subYear && 11 == cal1.get(Calendar.MONTH)))
            // 如果12月的最后一周横跨来年第一周的话则最后一周即算做来年的第一周
            && (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))) {
                flag = true;
        }
        return flag;
    }

    /**
     * 产生周序列,即得到当前时间所在的年度是第几周
     *
     * @return
     */
    public static String getSeqWeek() {
        Calendar c = Calendar.getInstance(Locale.CHINA);
        String week = Integer.toString(c.get(Calendar.WEEK_OF_YEAR));
        if (week.length() == 1) {
            week = "0" + week;
        }
        String year = Integer.toString(c.get(Calendar.YEAR));
        return year + week;
    }

    public static String getWeek(String sdate) {
        //再转换为时间
        Date date = stringToDate(sdate, DF_DATE);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        //hour中存的就是星期几了，其范围 1~7
        //1=星期日 7=星期六，其他类推
        return new SimpleDateFormat("EEEE").format(c.getTime());
    }

    /**
     * 根据一个日期，返回是星期几的字符串
     *
     * @param sdate
     * @return
     */
    public static String getWeekStr(String sdate) {
        String str = "";
        str = getWeek(sdate);
        if ("1".equals(str)) {
            str = "星期日";
        } else if ("2".equals(str)) {
            str = "星期一";
        } else if ("3".equals(str)) {
            str = "星期二";
        } else if ("4".equals(str)) {
            str = "星期三";
        } else if ("5".equals(str)) {
            str = "星期四";
        } else if ("6".equals(str)) {
            str = "星期五";
        } else if ("7".equals(str)) {
            str = "星期六";
        }
        return str;
    }

    /**
     * 两个时间之间的天数
     *
     * @param date1 "yyyy-MM-dd"
     * @param date2 "yyyy-MM-dd"
     * @return
     */
    public static long getDays(String date1, String date2) {
        if (date1 == null || date1.equals("") || date2 == null || date2.equals("")) {
            return 0;
        }
        // 转换为标准时间
        SimpleDateFormat myFormatter = new SimpleDateFormat(DF_DATE);
        Date date = null;
        Date mydate = null;
        try {
            date = myFormatter.parse(date1);
            mydate = myFormatter.parse(date2);
        } catch (Exception e) {
            return 0;
        }
        return (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
    }

    /**
     * 两个时间相差的毫秒数
     *
     * @param date   "yyyy-MM-dd"
     * @param mydate "yyyy-MM-dd"
     * @return
     */
    public static int getInterval(Date date, Date mydate) {
        if (date == null || mydate == null) {
            return 0;
        }
        return (int) ((date.getTime() - mydate.getTime()) / 1000);
    }

    /**
     * 计算年龄
     *
     * @param birthday
     * @return
     */
    public static int getAgeByBirth(Date birthday) {
        int age = 0;
        try {
            Calendar now = Calendar.getInstance();
            // 当前时间
            now.setTime(new Date());

            Calendar birth = Calendar.getInstance();
            birth.setTime(birthday);

            if (birth.after(now)) {
                //如果传入的时间，在当前时间的后面，返回0岁
                age = 0;
            } else {
                age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
                if (now.get(Calendar.DAY_OF_YEAR) > birth.get(Calendar.DAY_OF_YEAR)) {
                    age += 1;
                }
            }
            return age;
        } catch (Exception e) {
            //兼容性更强,异常后返回数据
            return 0;
        }
    }

    /**
     * 根据年龄获取出生年份
     *
     * @param years
     * @return
     */
    public static Date getYear(int years) {
        Date date = new Date();
        //取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        //把日期往后增加一天.整数往后推,负数往前移动
        calendar.add(Calendar.YEAR, -years);
        //这个时间就是日期往后推一天的结果
        date = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat(DF_DATE);
        String dateString = formatter.format(date);
        return stringToDate(dateString, DF_DATE);
    }

    /**
     * 根据年龄获取出生年份
     *
     * @param years
     * @return
     */
    public static long getYearLong(int years) {
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        //把日期往后增加一天.整数往后推,负数往前移动
        calendar.add(Calendar.YEAR, -years);
        date = calendar.getTime(); //这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat(DateTools.DF_DATE);
        String dateString = formatter.format(date);

        try {
            return formatter.parse(dateString).getTime();
        } catch (ParseException e) {
            LOG.error("DateTools.getYearLong错误！", e);
        }
        return 0;
    }

    /**
     * 根据年龄获取出生年份
     *
     * @param year
     * @return
     */
    public static long getYear(String year) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateTools.DF_DATE);
        try {
            return sdf.parse(year).getTime();
        } catch (ParseException e) {
            LOG.error("DateTools.getYear错误！", e);
        }
        return 0;
    }

    /**
     * 根据年龄获取出生年份
     *
     * @return
     */
    public static long getNowLong() {
        return System.currentTimeMillis();
    }


    /**
     * 获取后一天的毫秒数
     *
     * @param year
     * @return
     */
    public static long getEndYear(String year) {
        try {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(stringToDate(year, DateTools.DF_DATE));
            calendar.add(Calendar.DATE, 1);
            calendar.add(Calendar.SECOND, -1);
            return calendar.getTime().getTime();
        } catch (Exception e) {
            LOG.error("DateTools.getEndYear错误！", e);
        }
        return 0;
    }

    public static String longTimeStampToString(long value) {
        long days = value / (1000 * 60 * 60 * 24);
        long hours = (value % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long mins = (value % (1000 * 60 * 60)) / (1000 * 60);
        long secs = (value % (1000 * 60)) / (1000);

        return days + "天 " + hours + "小时 " + mins + "分 " + secs + "秒";
    }

    public static Date getOracleTimestampToDate(Object value) {
        try {
            Class clz = value.getClass();
            Method m = clz.getMethod("timestampValue");
            return new Date(((Timestamp) m.invoke(value)).getTime());

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * if(now - addtime < 7天)
     * @param addtime
     * @param now
     * @return
     */
    public static boolean isLatestWeek(Date addtime, Date now){
        Calendar calendar = Calendar.getInstance();  //得到日历
        calendar.setTime(now);//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, -7);  //设置为7天前
        Date before7days = calendar.getTime();   //得到7天前的时间
        if(before7days.getTime() < addtime.getTime()){
            return true;
        }else{
            return false;
        }
    }

    public static void main(String[] args) {
        LOG.info("DateTools.main:{}", getNowDateStr());
    }
}
