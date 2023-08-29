package com.bonestew.popmate.auth.application;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private static final long EXPIRATION_TIME_MS = 1000 * 60 * 60;
    private Date jwtExpiration = new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS);


    // JWT 서명 키를 가져오기 위한 설정
    @Value("${token.sign.key}")
    private String jwtSignKey;

    // 주어진 토큰에서 사용자 이름 추출
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 사용자 정보를 이용하여 토큰 생성
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }


    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // 주어진 토큰에서 특정 정보 추출
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    // 토큰의 모든 클레임(정보) 추출
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    // 토큰 생성
    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(jwtExpiration)
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }



    // 토큰이 만료되었는지 확인
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 토큰의 만료 시간 추출
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }



    // 서명에 사용되는 키 생성
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSignKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


}
