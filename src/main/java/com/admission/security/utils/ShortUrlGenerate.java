package com.admission.security.utils;

public class ShortUrlGenerate {

    public static String generate(String originalUrl) {
        return to62HEX(HashUtil.hash32(originalUrl));
    }


    /**
     * 10进制转26进制
     *
     * @param num
     * @return
     */
    private static String to62HEX(int num) {
        num = Math.abs(num);
        String chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

        StringBuilder sb = new StringBuilder();
        int remainder;

        while (num > 62 - 1) {
            remainder = Long.valueOf(num % 62).intValue();
            sb.append(chars.charAt(remainder));
            num = num / 62;
        }

        sb.append(chars.charAt(Long.valueOf(num).intValue()));
        return sb.reverse().toString();
    }
}


