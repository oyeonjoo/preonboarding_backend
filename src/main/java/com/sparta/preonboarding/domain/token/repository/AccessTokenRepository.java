package com.sparta.preonboarding.domain.token.repository;

import com.sparta.preonboarding.domain.token.entity.AccessToken;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {

  Optional<AccessToken> findByToken(String token);

  List<AccessToken> findAllByExpiredAtBefore(LocalDateTime now);
}
