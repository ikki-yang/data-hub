package com.issac.studio.app.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @description: 日期相关工具类
 * @file: DateUtil
 * @author: issac.young
 * @date: 2021/5/11 9:41 上午
 * @since: v1.0.0
 * @copyright (C), 1992-2021, issac
 */
public class DateUtil {
    public static String todayAdd(Integer offset, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date added = add(new Date(), offset);
        return sdf.format(added);
    }

    public static String dateAdd(String dateStr, String sourceFormat, Integer offset,
                                 String targetFormat) throws Exception {
        SimpleDateFormat sourceSdf = new SimpleDateFormat(sourceFormat);
        Date date = sourceSdf.parse(dateStr);
        Date added = add(date, offset);
        return new SimpleDateFormat(targetFormat).format(added);
    }

    public static String today(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }

    public static Date add(Date date, Integer offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, offset);
        return calendar.getTime();
    }

    public static String lastDayOfLastMonth(String todayStr, String sourceFormat,
                                            String targetFormat) throws Exception {
        SimpleDateFormat sourceSdf = new SimpleDateFormat(sourceFormat);
        Date parse = sourceSdf.parse(todayStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parse);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH - 1));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return new SimpleDateFormat(targetFormat).format(calendar.getTime());
    }

    public static String lastMonth(String todayStr, String sourceFormat,
                                   String targetFormat) throws Exception {
        return lastDayOfLastMonth(todayStr, sourceFormat, targetFormat);
    }
}
