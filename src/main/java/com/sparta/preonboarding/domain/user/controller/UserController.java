package com.sparta.preonboarding.domain.user.controller;

import com.sparta.preonboarding.domain.user.dto.SignRequest;
import com.sparta.preonboarding.domain.user.dto.SignupRequest;
import com.sparta.preonboarding.domain.user.dto.SignupResponse;
import com.sparta.preonboarding.domain.user.service.UserService;
import com.sparta.preonboarding.global.jwt.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "User API")
@RequestMapping("/api/v1/users")
public class UserController {

  private final UserService userService;

  @PostMapping("/signup")
  @Operation(summary = "회원가입", description = "새로운 회원을 등록합니다.")
  public ResponseEntity<SignupResponse> signup(@RequestBody @Validated SignupRequest dto) {
    SignupResponse response = userService.signup(dto);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(response);
  }

  @PostMapping("/sign")
  @Operation(summary = "로그인", description = "회원 로그인을 수행합니다.")
  public ResponseEntity<Void> sign(@RequestBody @Validated SignRequest dto) {
    String token = userService.sign(dto);

    return ResponseEntity.status(HttpStatus.OK)
        .header(JwtUtil.AUTHORIZATION_HEADER, token).build();
  }
}
