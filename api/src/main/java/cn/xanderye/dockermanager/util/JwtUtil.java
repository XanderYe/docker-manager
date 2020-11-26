package cn.xanderye.dockermanager.util;

import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

/**
 * Created on 2020/11/26.
 *
 * @author XanderYe
 */
public class JwtUtil {

    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(MD5Util.md5("XanderYe").getBytes());

    public static String encode(String username){
        return Jwts.builder()
                .setSubject(username)
                .compressWith(CompressionCodecs.GZIP)
                .signWith(SECRET_KEY)
                .compact();
    }

    public static String decode(String jwt) throws ExpiredJwtException{
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject();
    }
}
