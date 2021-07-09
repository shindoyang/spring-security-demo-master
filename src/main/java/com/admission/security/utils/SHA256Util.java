package com.admission.security.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Copyright (C), 2006-2020, xy
 * FileName: SHA256Util
 * sha256加密
 *
 * @author wangsheng
 * @version 1.0.0
 * @Date 2020/8/12 14:03
 */
public class SHA256Util {

    /**
     * 利用java原生的摘要实现SHA256加密
     *
     * @param str 加密后的报文
     * @return
     */
    public static String getSHA256(String str) {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    /**
     * 获取文件sha256值
     *
     * @param file
     * @return
     */
    public static String getFileSHA256(File file) {
        String str = "";
        try {
            str = getHash(new FileInputStream(file), "SHA-256");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 获取文件sha256值
     *
     * @param in
     * @return
     */
    public static String getFileSHA256(InputStream in) {
        String str = "";
        try {
            str = getHash(in, "SHA-256");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    private static String getHash(InputStream in, String hashType) throws Exception {
        byte buffer[] = new byte[1024];
        MessageDigest md5 = MessageDigest.getInstance(hashType);
        for (int numRead = 0; (numRead = in.read(buffer)) > 0; ) {
            md5.update(buffer, 0, numRead);
        }
        in.close();
        return byte2Hex(md5.digest());
    }

    /**
     * 将byte转为16进制
     *
     * @param bytes
     * @return
     */
    private static String byte2Hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(Integer.toHexString(b & 0xFF));
        }
        return sb.toString();
    }

}
