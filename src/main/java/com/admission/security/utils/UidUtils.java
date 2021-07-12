package com.admission.security.utils;

import com.admission.security.constant.RedisConstant;
import com.alibaba.fastjson.JSONArray;

public class UidUtils {

    /**
     * 获取短链
     */
    public static String getUid(JSONArray cacheWholeUidArr, String host, String mobile) {
        String uid = ShortUrlGenerate.generate(String.format(RedisConstant.USER_SHORT_URL, host, IdUtils.randomUUID(), mobile));
        if (null != cacheWholeUidArr && cacheWholeUidArr.contains(uid)) {
            uid = getUid(cacheWholeUidArr, host, mobile);
        }
        return uid;
    }
}
