package com.spring.security.utils;

import org.apache.http.util.TextUtils;

import java.util.regex.Pattern;

public class MobileUtil {

    /**
     * 中国移动 - 号码、虚拟号段
     **/
    static String[] cmccNumArray = {"139", "138", "137", "136", "135", "134", "147", "150", "151", "152", "157", "158", "159", "178", "182", "183", "184", "187", "188", "195", "198", "1703", "1705", "1706", "165"};

    /**
     * 中国电信 - 号码、虚拟号段
     **/
    static String[] ctccNumArray = {"133", "149", "153", "173", "177", "180", "181", "189", "199", "191", "1700", "1701", "1702", "162"};

    /**
     * 中国联通 - 号码、虚拟号段
     **/
    static String[] cuccNumArray = {"130", "131", "132", "155", "156", "185", "186", "145", "175", "176", "166", "140", "171", "1707", "1708", "1709", "167"};

    public static final Pattern MOBILE_PATTERN = Pattern.compile("(1\\d{10})");

    public static boolean isMobileNO(String mobiles) {
        String telRegex = "[1][3456789]\\d{9}";
        // "[1]"代表第1位为数字1，"[3456789]"代表第二位可以为3、4、5、6、7、8、9中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        } else {
            return mobiles.matches(telRegex);
        }
    }

    public static void main(String[] args) {
        System.out.println(isMobileNO("15272351416"));
    }
}
