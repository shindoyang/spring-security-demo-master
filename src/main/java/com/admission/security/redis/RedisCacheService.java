package com.admission.security.redis;

import com.admission.security.constant.RedisConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class RedisCacheService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public String getUid(String account) {
        //获取缓存的已经处理过的手机号
        String cacheMobileKey = String.format(RedisConstant.USER_CACHE_MOBILES, account);
        String cacheUidKey = String.format(RedisConstant.USER_CACHE_UIDS, account);

        String cacheMobiles = stringRedisTemplate.opsForValue().get(cacheMobileKey);
        //获取缓存的已经生成的手机号-uid对应关系
        String cacheUids = stringRedisTemplate.opsForValue().get(cacheUidKey);

        JSONArray cacheMobileArr = new JSONArray();
        Map<String, String> cacheUidMap = new HashMap();
        if (Strings.isNotEmpty(cacheMobiles)) {
            cacheMobileArr = JSONObject.parseArray(cacheMobiles);
        }
        if (Strings.isNotEmpty(cacheUids)) {
            cacheUidMap = (Map) JSON.parse(cacheUids);
        }
        log.info("已处理过的手机号个数：{}", null != cacheMobileArr ? cacheMobileArr.size() : 0);
        log.info("已缓存过的uid个数：{}", null != cacheUidMap ? cacheUidMap.size() : 0);
        return null;
    }
}
