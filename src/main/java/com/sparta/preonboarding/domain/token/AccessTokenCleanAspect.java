package com.sparta.preonboarding.domain.token;

import com.sparta.preonboarding.domain.token.service.AccessTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccessTokenCleanAspect {

  private final AccessTokenService accessTokenService;

  @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
  public void cleanupExpiredTokens() {
    accessTokenService.removeExpiredTokens();
  }
}
