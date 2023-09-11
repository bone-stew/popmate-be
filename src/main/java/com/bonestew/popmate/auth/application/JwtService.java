package com.bonestew.popmate.auth.application;

import com.bonestew.popmate.auth.domain.PopmateUser;
import com.bonestew.popmate.auth.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JwtService {

    private static final long EXPIRATION_TIME_MS = 1000L * 60 * 60 * 24 * 30;
    private static final String USER_ID = "userId";
    private static final String NAME = "userName";
    private final Date jwtExpiration = new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS);

    // JWT 서명 키를 가져오기 위한 설정
    @Value("${token.sign.key}")
    private String jwtSignKey;

    public String generateToken(User user) {
        return Jwts.builder()
            .setSubject(user.getEmail())
            .claim(USER_ID, user.getUserId())
            .claim(NAME, user.getName())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(jwtExpiration)
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.");
        }
        log.info(token);
        return false;
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSignKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public User getUserInfo(String jwtToken) {
        Claims claims = Jwts
            .parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(jwtToken)
            .getBody();
        User user = new User();
        user.setUserId(claims.get(USER_ID, Long.class));
        user.setName(claims.get(NAME, String.class));
        return user;
    }
}
