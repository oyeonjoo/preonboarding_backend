package com.sparta.preonboarding.global.jwt;

import com.sparta.preonboarding.domain.user.entity.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class JwtUtil {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String BEARER_PREFIX = "Bearer ";

    private final long TOKEN_TIME = 30 * 60 * 1000L;
//    private final long ACCESS_TOKEN_TIME = 30 * 60 * 100L;
//    private final long REFRESH_TOKEN_TIME = 30 * 60 * 1000L;

    @Value("${jwt.secret.key}")
    private String secretKey;

    private Key key;
    private final Set<String> tokenBlackList = new HashSet<>();

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String getTokenFromHeader(HttpServletRequest request) {
        String tokenValue = request.getHeader(AUTHORIZATION_HEADER);
        log.info(tokenValue);
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        if(tokenBlackList.contains(token)) {
            return false;
        }
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
                .getBody();
    }

    public String createToken(String username, UserRole role) {
        Date expireDate = createExpireDate(TOKEN_TIME);

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username)
                        .claim("username", username)
                        .setExpiration(expireDate)
                        .setIssuedAt(new Date())
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }
//    public String createAccessToken(String username, UserRole role) {
//            Date expireDate = createExpireDate(ACCESS_TOKEN_TIME);
//
//            return BEARER_PREFIX +
//                    Jwts.builder()
//                            .setSubject(username)
//                            .claim("username", username)
//                            .setExpiration(expireDate)
//                            .setIssuedAt(new Date())
//                            .signWith(key, signatureAlgorithm)
//                            .compact();
//    }

//    public String createRefreshToken(String username, UserRole role) {
//        Date expireDate = createExpireDate(REFRESH_TOKEN_TIME);
//
//        return BEARER_PREFIX +
//                Jwts.builder()
//                        .setSubject(username)
//                        .claim("username", username)
//                        .setExpiration(expireDate)
//                        .setIssuedAt(new Date())
//                        .signWith(key, signatureAlgorithm)
//                        .compact();
//    }

    private Date createExpireDate(long expireDate) {
        long curTime = (new Date()).getTime();
        return new Date(curTime + expireDate);
    }
}
