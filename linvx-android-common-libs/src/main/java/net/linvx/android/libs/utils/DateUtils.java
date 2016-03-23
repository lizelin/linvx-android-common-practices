package net.linvx.android.libs.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by lizelin on 16/2/5.
 * 日期的相关常用函数
 */
public class DateUtils {
    private DateUtils() {
    }

    /**
     * 转换字符串为Date
     *
     * @param gmt，格式："Sun, 13-Dec-2082 19:41:09 GMT"
     * @return
     * @throws ParseException
     */
    public static Date parseGMTString(String gmt) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("E, d-MMM-yyyy HH:mm:ss 'GMT'", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = sdf.parse(gmt);
        return date;
    }

    public static String dateToGMTString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("E, d-MMM-yyyy HH:mm:ss 'GMT'", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(date);
    }

    public static long diffMilliseconds(Date before, Date after) {
        return after.getTime() - before.getTime();
    }

    public static long diffSeconds(Date before, Date after) {
        return diffMilliseconds(before, after) / 1000L;
    }

    public static long diffMinutes(Date before, Date after) {
        return diffMilliseconds(before, after) / 1000L / 60L;
    }

    public static long diffHours(Date before, Date after) {
        return diffMilliseconds(before, after) / 1000L / 60L / 60L;
    }

    public static long diffDays(Date before, Date after) {
        return diffMilliseconds(before, after) / 1000L / 60L / 60L / 24L;
    }


    public static String getTimeStamp() {
        return String.valueOf(System.currentTimeMillis());
    }

    public static String getTimeStampFormatString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒.SSSS ");
        return sdf.format(new Date());
    }
}
