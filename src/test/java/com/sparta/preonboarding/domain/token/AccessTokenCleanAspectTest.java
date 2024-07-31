package com.sparta.preonboarding.domain.token;

import com.sparta.preonboarding.domain.token.entity.AccessToken;
import com.sparta.preonboarding.domain.token.repository.AccessTokenRepository;
import com.sparta.preonboarding.domain.token.service.AccessTokenService;
import com.sparta.preonboarding.domain.user.entity.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class AccessTokenCleanAspectTest {

  @Autowired
  private AccessTokenService accessTokenService;

  @Autowired
  private AccessTokenRepository accessTokenRepository;

  @BeforeEach
  public void setUp() {
    // 데이터 초기화 또는 정리
    accessTokenRepository.deleteAll();
  }

  @Test
  @Transactional
  public void testCleanupExpiredTokens() {
    // Given: 만료된 토큰 저장
    AccessToken expiredToken = AccessToken.builder()
        .token("expiredToken")
        .username("user")
        .role(UserRole.USER)
        .issuedAt(LocalDateTime.now().minusDays(2))
        .expiredAt(LocalDateTime.now().minusDays(1))
        .isBlacklisted(false)
        .build();
    accessTokenRepository.save(expiredToken);

    // When: cleanupExpiredTokens 호출
    accessTokenService.removeExpiredTokens();

    // Then: 만료된 토큰이 삭제되었는지 검증
    boolean tokenExists = accessTokenRepository.findByToken("expiredToken").isPresent();
    assertTrue(!tokenExists, "Expired token should be removed");
  }
}

