package com.xqd.mylibrary.utlis;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Administrator on 2017/8/11.
 */

public class DateUtils {
    public static final int WEEKTYPE = 1;
    public static final int MONTHYPE = 2;
    public static final int YEARTYPE = 3;

    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat FORMAT_DATE_1 = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

    /**
     * 通过时间戳获取默认格式的时间
     *
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }

    /**
     * 通过时间戳获取指定格式的时间
     *
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * 通过时间戳获取指定格式的时间
     *
     * @param timeInMillis
     * @param format
     * @return
     */
    public static String getTime(long timeInMillis, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return getTime(timeInMillis, dateFormat);
    }

    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static String getCurrentTimeInString(String format) {
        return getTime(getCurrentTimeInLong(), format);
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * yyyy-MM-dd HH:mm的时间转换为时间戳
     *
     * @param time
     * @return
     */
    public static long getTimeStamp(String time) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(time);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 根据指定的格式化后的时间转换为时间戳
     *
     * @param time
     * @param format
     * @return
     */
    public static long getTimeStamp(String time, String format) {
        try {
            Date date = new SimpleDateFormat(format).parse(time);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 根据指定的格式化后的时间转换为时间戳
     *
     * @param time
     * @param format
     * @return
     */
    public static long getTimeStamp(String time, SimpleDateFormat format) {
        try {
            Date date = format.parse(time);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 调此方法输入所要转换的时间输入例如（"yyyy-MM-dd HH:mm:ss"）返回时间戳(不包括毫秒)
     *
     * @param time
     * @return
     */
    public static String getTimestamp(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }


    /**
     * 并用分割符把时间分成时间数组
     *
     * @param time
     * @return
     */
    public static String[] timestamp(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        String[] fenge = times.split("[年月日时分秒]");
        return fenge;
    }

    /**
     * 分割符把时间分成时间数组
     *
     * @param time
     * @return
     */
    public String[] division(String time) {
        String[] fenge = time.split("[年月日时分秒]");
        return fenge;
    }

    /**
     * 输入格式化后的日期，返回（星期数）
     *
     * @param time
     * @return
     */
    public String week(String time, String format) {
        Date date = null;
        SimpleDateFormat sdr = new SimpleDateFormat(format);
        int mydate = 0;
        String week = null;
        try {
            date = sdr.parse(time);
            Calendar cd = Calendar.getInstance();
            cd.setTime(date);
            mydate = cd.get(Calendar.DAY_OF_WEEK);
            // 获取指定日期转换成星期几
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (mydate == 1) {
            week = "星期日";
        } else if (mydate == 2) {
            week = "星期一";
        } else if (mydate == 3) {
            week = "星期二";
        } else if (mydate == 4) {
            week = "星期三";
        } else if (mydate == 5) {
            week = "星期四";
        } else if (mydate == 6) {
            week = "星期五";
        } else if (mydate == 7) {
            week = "星期六";
        }
        return week;
    }

    /**
     * 输入时间戳 返回（星期数）
     *
     * @param time
     * @return
     */
    public static String weekOne(long time) {
        int mydate = 0;
        String week = null;
        try {
            Calendar cd = Calendar.getInstance();
            cd.setTime(new Date(time));
            mydate = cd.get(Calendar.DAY_OF_WEEK);
            // 获取指定日期转换成星期几
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (mydate == 1) {
            week = "星期日";
        } else if (mydate == 2) {
            week = "星期一";
        } else if (mydate == 3) {
            week = "星期二";
        } else if (mydate == 4) {
            week = "星期三";
        } else if (mydate == 5) {
            week = "星期四";
        } else if (mydate == 6) {
            week = "星期五";
        } else if (mydate == 7) {
            week = "星期六";
        }
        return week;
    }

    /**
     * 输入时间戳 返回（周几）
     *
     * @param timeStamp
     * @return
     */
    public static String getWeek(long timeStamp) {
        int mydate = 0;
        String week = null;
        Calendar cd = Calendar.getInstance();
        cd.setTime(new Date(timeStamp));
        mydate = cd.get(Calendar.DAY_OF_WEEK);
        // 获取指定日期转换成星期几
        if (mydate == 1) {
            week = "周日";
        } else if (mydate == 2) {
            week = "周一";
        } else if (mydate == 3) {
            week = "周二";
        } else if (mydate == 4) {
            week = "周三";
        } else if (mydate == 5) {
            week = "周四";
        } else if (mydate == 6) {
            week = "周五";
        } else if (mydate == 7) {
            week = "周六";
        }
        return week;
    }

    public static String getWeek(int mydate) {

        String week = null;

        // 获取指定日期转换成星期几
        if (mydate == 7) {
            week = "日";
        } else if (mydate == 1) {
            week = "一";
        } else if (mydate == 2) {
            week = "二";
        } else if (mydate == 3) {
            week = "三";
        } else if (mydate == 4) {
            week = "四";
        } else if (mydate == 5) {
            week = "五";
        } else if (mydate == 6) {
            week = "六";
        }
        return week;
    }

//    public static void main(String[] args) {
//        System.out.println(getTime(getThisWeekMonday(new Date()).getTime()));
//    }

    //获取这周周一的时间
    public static Date getThisWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        return cal.getTime();
    }

    public static Date geLastWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getThisWeekMonday(date));
        cal.add(Calendar.DATE, -7);
        return cal.getTime();
    }

    public static Date getNextWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getThisWeekMonday(date));
        cal.add(Calendar.DATE, 7);
        return cal.getTime();
    }

    /**
     * 得到当天起始时间
     *
     * @return
     */
    public static long getTimeOfDayStart() {
        Calendar currentDate = new GregorianCalendar();

        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        return currentDate.getTimeInMillis();
    }

    /**
     * 当天结束时间
     *
     * @return
     */
    public static long getTimeOfDayOver() {
        Calendar currentDate = new GregorianCalendar();

        currentDate.set(Calendar.HOUR_OF_DAY, 23);
        currentDate.set(Calendar.MINUTE, 59);
        currentDate.set(Calendar.SECOND, 59);
        return currentDate.getTimeInMillis();
    }

    /**
     * 得到当前月
     *
     * @return
     */
    public static int getTodayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }


    /**
     * 得到今天是星期几
     *
     * @return
     */
    public static int getTodayisWeek() {
        Calendar calendar = Calendar.getInstance();
        int todayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (todayOfWeek == 0) {
            todayOfWeek = 7;
        }
        return todayOfWeek;
    }

    /**
     * 得到当天几号
     *
     * @return
     */
    public static int getTodayOfDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 当周起始的那天是几号
     *
     * @return
     */
    public static int getTodayofWeek() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.clear(Calendar.MINUTE);
        ca.clear(Calendar.SECOND);
        ca.clear(Calendar.MILLISECOND);
//        ca.set(Calendar.DAY_OF_WEEK, ca.getFirstDayOfWeek());
        ca.set(Calendar.DAY_OF_WEEK, ca.MONDAY);
        return ca.get(Calendar.DATE);
    }

    /**
     * 获取当周起始时间
     *
     * @return
     */
    public static long getTimeOfWeekStart() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.clear(Calendar.MINUTE);
        ca.clear(Calendar.SECOND);
        ca.clear(Calendar.MILLISECOND);
//        ca.set(Calendar.DAY_OF_WEEK, ca.getFirstDayOfWeek());
        ca.set(Calendar.DAY_OF_WEEK, ca.MONDAY);
        return ca.getTimeInMillis();
    }

    /**
     * 获取当月起始时间
     *
     * @return
     */
    public static long getTimeOfMonthStart() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.clear(Calendar.MINUTE);
        ca.clear(Calendar.SECOND);
        ca.clear(Calendar.MILLISECOND);
        ca.set(Calendar.DAY_OF_MONTH, 1);
        return ca.getTimeInMillis();
    }

    /**
     * 获取当年起始间
     *
     * @return
     */
    public static long getTimeOfYearStart() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.clear(Calendar.MINUTE);
        ca.clear(Calendar.SECOND);
        ca.clear(Calendar.MILLISECOND);
        ca.set(Calendar.DAY_OF_YEAR, 1);
        return ca.getTimeInMillis();
    }


    /**
     * 得到指定类型的结束时间
     *
     * @param type 1当周 2当月 3当年
     * @return
     */
    public static long getTimeOver(int type) {
        long starTime = 0;
        switch (type) {
            case WEEKTYPE:
                starTime = getTimeOfWeekStart();
                break;
            case MONTHYPE:
                starTime = getTimeOfMonthStart();
                break;
            case YEARTYPE:
                starTime = getTimeOfYearStart();
                break;
        }
        return starTime + (86400000 * 7);
    }


    /**
     * 获取当前周日期
     */
    public static String getCurWeekend(int dayIndex) {
        Calendar calendar = Calendar.getInstance();
        getWeekDay(calendar, dayIndex);
        return DATE_FORMAT_DATE.format(calendar.getTime());
    }

    /**
     * 获取本周日期
     *
     * @param calendar
     * @param dayIndex 周几 1 - 7
     */
    private static void getWeekDay(Calendar calendar, int dayIndex) {
        calendar.setTime(new Date());
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        calendar.add(Calendar.DATE, -day_of_week + dayIndex);
    }

    public static String getMonthFirstDay() {
        // 获取前月的第一天
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        return DATE_FORMAT_DATE.format(cale.getTime());
    }

    public static String getMonthEndDay() {
        Calendar cale = Calendar.getInstance();
        cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        return DATE_FORMAT_DATE.format(cale.getTime());
    }

    public static String formatDateStr(String time, String format1, String format2) {
        Date date = new Date(getTimeStamp(time, format1));
        return new SimpleDateFormat(format2).format(date);
    }

    public static String getTimeForSS(long ss, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        String hms = formatter.format(ss);

        return hms;
    }

    public static String getTimeForSS2(long ss) {

        long secondStr = ss % 60;
        long minute = (ss - secondStr) / 60;
        long minuteStr = minute % 60;
        long hour = (minute - minuteStr) / 60;

        return formatTime(hour) + ":" + formatTime(minuteStr) + ":" + formatTime(secondStr);
    }

    public static String formatTime(long time) {
        if (time < 10) {
            return "0" + time;
        } else {
            return time + "";
        }
    }

}

