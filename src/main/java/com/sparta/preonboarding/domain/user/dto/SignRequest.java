package com.sparta.preonboarding.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignRequest {

  @NotBlank(message = "이름을 입력하세요.")
  private String username;

  @NotBlank(message = "비밀번호를 입력하세요.")
  private String password;
}
