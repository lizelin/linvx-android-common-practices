package net.linvx.android.libs.utils;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lizelin on 16/2/5.
 * String的一些常用函数
 */
public class StringUtils {
    private StringUtils() {
    }

    /**
     * 判断是否为null或者为空字符串
     *
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        return null == s || s.length() == 0;
    }

    /**
     * 判断是否非空字符串并且非null
     *
     * @param s
     * @return
     */
    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    /**
     * 仿oracle nvl函数：
     *
     * @param s
     * @param defaultValue
     * @return 如果s为空，则返回缺省defaultValue，否则返回s
     */
    public static String nvlString(String s, String defaultValue) {
        return isEmpty(s) ? defaultValue : s;
    }

    /**
     * 判断字符串s是否包含find，忽略大小写
     *
     * @param s
     * @param find
     * @return
     */
    public static boolean containsIgnoreCase(String s, String find) {
        if (isEmpty(s) || isEmpty(find))
            return false;
        return s.toLowerCase(Locale.getDefault()).contains(find.toLowerCase(Locale.getDefault()));
    }

    /**
     * 判断字符串s是否以prefix开头，忽略大小写
     *
     * @param s
     * @param prefix
     * @return
     */
    public static boolean startWithIgnoreCase(String s, String prefix) {
        if (isEmpty(s) || isEmpty(prefix))
            return false;
        return s.toLowerCase(Locale.getDefault()).startsWith(prefix.toLowerCase(Locale.getDefault()));
    }

    /**
     * 根据字符集编码获取字符串s的byte数组
     *
     * @param s
     * @param charset
     * @return
     */
    public static byte[] getBytes(String s, String charset) {
        if (isEmpty(s) || isEmpty(charset))
            return null;
        byte[] t = null;
        try {
            t = s.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 获取字符串s的byte数组，缺省认为是utf-8编码
     *
     * @param s
     * @return
     */
    public static byte[] getBytes(String s) {
        return getBytes(s, "UTF-8");
    }

    /**
     * 将byte数组转为字符串
     *
     * @param b
     * @param charset
     * @return
     */
    public static String getString(byte[] b, String charset) {
        if (b == null || b.length == 0 || isEmpty(charset))
            return "";
        String s = "";
        try {
            s = new String(b, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * 将byte数组转为字符串，默认为utf-8编码
     *
     * @param b
     * @return
     */
    public static String getString(byte[] b) {
        return getString(b, "UTF-8");
    }

    /**
     * 检查是否包含中文
     *
     * @param sequence
     * @return
     */
    public static boolean checkChinese(String sequence) {
        final String format = "[\\u4E00-\\u9FA5\\uF900-\\uFA2D]";
        boolean result = false;
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(sequence);
        result = matcher.find();
        return result;
    }

    /**
     * 检测字符串中只能包含：中文、数字、下划线(_)、横线(-)
     * @param sequence
     * @return
     */
    public static boolean checkNickname(String sequence) {
        final String format = "[^\\u4E00-\\u9FA5\\uF900-\\uFA2D\\w-_]";
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(sequence);
        return !matcher.find();
    }
}
