package com.sparta.preonboarding.domain.user.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupResponse {

  private String username;
  private String nickname;
  private List<AuthorityDto> authorities;
}
