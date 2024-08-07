package com.sparta.preonboarding.domain.token.entity;

import com.sparta.preonboarding.domain.user.entity.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "token_blacklist")
public class AccessToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String token;

  private String username;

  @Enumerated(EnumType.STRING)
  private UserRole role;

  @CreatedDate
  @Column(updatable = false)
  private LocalDateTime issuedAt;

  @Column(nullable = false)
  private LocalDateTime expiredAt;

  private boolean isBlacklisted = false;

  @Builder
  public AccessToken(
      String token,
      String username,
      UserRole role,
      LocalDateTime issuedAt,
      LocalDateTime expiredAt,
      boolean isBlacklisted
  ) {
    this.token = token;
    this.username = username;
    this.role = role;
    this.issuedAt = issuedAt;
    this.expiredAt = expiredAt;
    this.isBlacklisted = isBlacklisted;
  }

  public void blacklistToken() {
    this.isBlacklisted = true;
  }
}
