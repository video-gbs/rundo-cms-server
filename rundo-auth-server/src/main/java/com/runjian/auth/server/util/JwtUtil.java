package com.runjian.auth.server.util;


import io.jsonwebtoken.*;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.UUID;

/**
 * JWT工具类
 */
public class JwtUtil {

    //默认token有效期为 一个小时 60 * 60 *1000
    public static final Long JWT_TTL = 60 * 60 * 1000L;
    //设置秘钥明文：密钥位数不够，必须大于256位，一个字符按照8位算，至少32个字符。
    public static final String JWT_KEY = "jinan_20220511jinan_20220511jinan_20220511jinan_20220511";

    /**
     * 生成jtw
     *
     * @param subject token中要存放的数据（json格式）
     * @return
     */
    public static String createJWT(String subject) {
        JwtBuilder builder = getJwtBuilder(subject, null, getUUID());// 设置过期时间
        return builder.compact();
    }

    /**
     * 生成jtw
     *
     * @param subject   token中要存放的数据（json格式）
     * @param ttlMillis token超时时间
     * @return
     */
    public static String createJWT(String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, getUUID());// 设置过期时间
        return builder.compact();
    }

    /**
     * 创建token
     *
     * @param id
     * @param subject
     * @param ttlMillis
     * @return
     */
    public static String createJWT(String id, String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, id);// 设置过期时间
        return builder.compact();
    }


    /**
     * 解析token
     *
     * @param jwt
     * @return
     */
    public static Claims parseJWT(String jwt) {
        Claims claims;
        SecretKey secretKey = generalKey();
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build().parseClaimsJws(jwt).getBody();
        } catch (JwtException e) {
            claims = null;
            e.printStackTrace();
        }
        return claims;
    }

    public static Boolean isTokenExpired(String jwt) {
        SecretKey secretKey = generalKey();
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build().parseClaimsJws(jwt).getBody()
                .getExpiration().before(new Date());
    }

    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if (ttlMillis == null) {
            ttlMillis = JwtUtil.JWT_TTL;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        return Jwts.builder()
                .setId(uuid)              //唯一的ID
                .setSubject(subject)   // 主题  可以是JSON数据
                .setIssuer("runjiangufen")     // 签发者
                .setIssuedAt(now)      // 签发时间
                .signWith(generalKey(), SignatureAlgorithm.HS256) //使用HS256对称加密算法签名, 第二个参数为秘钥
                .setExpiration(expDate);
    }


    /**
     * 生成加密后的秘钥 secretKey
     *
     * @return
     */
    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.decodeBase64(JWT_KEY);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "HmacSHA256");
    }

    private static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}