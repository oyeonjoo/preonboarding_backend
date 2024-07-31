package com.sparta.preonboarding.domain.token.service;

import com.sparta.preonboarding.domain.token.entity.AccessToken;
import com.sparta.preonboarding.domain.token.repository.AccessTokenRepository;
import com.sparta.preonboarding.domain.user.entity.UserRole;
import com.sparta.preonboarding.global.jwt.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AccessTokenService {

  private final AccessTokenRepository accessTokenRepository;
  private JwtUtil jwtUtil;

  public String saveToken(String username, UserRole role) {
    // JWT 토큰 생성
    String token = createToken(username, role);

    // 만료 시간 계산
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime expiredAt = now.plusSeconds(jwtUtil.getTokenExpirationInSeconds());

    // 토큰 엔티티 생성 및 저장
    AccessToken accessToken = AccessToken.builder()
        .token(token)
        .username(username)
        .role(role)
        .issuedAt(now)
        .expiredAt(expiredAt)
        .isBlacklisted(false)
        .build();

    accessTokenRepository.save(accessToken);

    return token;
  }

  private String createToken(String username, UserRole role) {
    return jwtUtil.createToken(username, role);
  }

  public void blacklistToken(String tokenId) {
    AccessToken accessToken = accessTokenRepository.findById(Long.parseLong(tokenId))
        .orElseThrow(() -> new EntityNotFoundException("Token not found, 존재하지 않는 JWT token 입니다."));

    accessToken.blacklistToken();
    accessTokenRepository.save(accessToken);
  }

  public boolean isTokenBlacklisted(String token) {
    return accessTokenRepository.findByToken(token)
        .map(AccessToken::isBlacklisted)
        .orElse(false);
  }

  public void removeExpiredTokens() {
    LocalDateTime now = LocalDateTime.now();
    List<AccessToken> expiredTokens = accessTokenRepository.findAllByExpiredAtBefore(now);
    accessTokenRepository.deleteAll(expiredTokens);
  }
}
