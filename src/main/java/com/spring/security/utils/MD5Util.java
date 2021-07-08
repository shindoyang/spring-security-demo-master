package com.spring.security.utils;

import java.math.BigInteger;
import java.security.MessageDigest;

public class MD5Util {

    public static String getStringMD5(String str) {

        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            //一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方）
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static void main(String[] args) {
        //123456  --- 1c1f4f0008c032a01dbb17bddaca9b7a
        //admin   --- 6607f51871353c72ad66bfcb21635565
        String password = "admin";

        /*
        原用户数据：
        user1-(123456): $2a$10$47lsFAUlWixWG17Ca3M/r.EPJVIb7Tv26ZaxhzqN65nXVcAhHQM4i
        user2-(123456): $2a$10$uSLAeON6HWrPbPCtyqPRj.hvZfeM.tiVDZm24/gRqm4opVze1cVvC
         */

        String reqPassword = MD5Util.getStringMD5("admission:" + password);
        System.out.println(reqPassword);
    }
}
