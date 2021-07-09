package com.admission.security.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {

    //秘钥 盐
    private static final String SECRET = "SmyC47re$ML%F72x6koqyUQ0XLQOasdsad";

    /**
     * 生成token header.payload.sing
     *
     * @param map
     * @param expired
     * @return
     */
    public static String generateTokenExpireInMinutes(Map<String, String> map, Integer expired) {
        Calendar instance = Calendar.getInstance();
        //设置过期时间单位:秒
        instance.add(Calendar.SECOND, expired);
        //创建jwt builder
        JWTCreator.Builder builder = JWT.create();
        //payload
        map.forEach((k, v) -> {
            builder.withClaim(k, v);
        });
        //制定令牌过期时间
        String token = builder.withExpiresAt(instance.getTime())
                //sign
                .sign(Algorithm.HMAC256(SECRET));
        return token;
    }

    /**
     * 验证token 合法性 || 获取token信息方法
     *
     * @param token
     */
    public static DecodedJWT verify(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
    }

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", "1");
        map.put("loginName", "admin");
        String token = generateTokenExpireInMinutes(map, 60);
        System.out.println(token);

        DecodedJWT tokenInfo = verify("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpbk5hbWUiOiJhZG1pbiIsImV4cCI6MTYyNTYyODQ0MiwidXNlcklkIjoiMSJ9.shRNDWemwxU-fUTGgcIqVSwOdTpRWbpkiPrGVqlXyp0");
        String userId = tokenInfo.getClaim("userId").asString();
        String loginName = tokenInfo.getClaim("loginName").asString();

        System.out.println(userId);
        System.out.println(loginName);
    }
}
