package com.sparta.preonboarding.domain.user.service;

import com.sparta.preonboarding.domain.user.dto.AuthorityDto;
import com.sparta.preonboarding.domain.user.dto.SignRequest;
import com.sparta.preonboarding.domain.user.dto.SignupRequest;
import com.sparta.preonboarding.domain.user.dto.SignupResponse;
import com.sparta.preonboarding.domain.user.entity.User;
import com.sparta.preonboarding.domain.user.entity.UserRole;
import com.sparta.preonboarding.domain.user.repository.UserRepository;
import com.sparta.preonboarding.global.exception.custom.PasswordNotMatchedException;
import com.sparta.preonboarding.global.jwt.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  public SignupResponse signup(SignupRequest dto) {
    String username = dto.getUsername();
    String password = passwordEncoder.encode(dto.getPassword());
    String nickname = dto.getNickname();

    validateUsername(username);

    User user = new User(username, password, nickname);
    userRepository.save(user);

    return new SignupResponse(username, nickname, mapAuthorities(List.of(UserRole.USER)));
  }

  public String sign(SignRequest dto) {
    User user = userRepository.findByUsername(dto.getUsername())
        .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자 입니다."));

    validateUserPassword(user, dto.getPassword());

    return jwtUtil.createToken(user.getUsername(), user.getRole());
  }

  private void validateUsername(String username) {
    Optional<User> checkUsername = userRepository.findByUsername(username);
    if (checkUsername.isPresent()) {
      throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
    }
  }

  private List<AuthorityDto> mapAuthorities(List<UserRole> roles) {
    List<AuthorityDto> authorities = new ArrayList<>();
    for (UserRole role : roles) {
      authorities.add(new AuthorityDto(role.getAuthority()));
    }
    return authorities;
  }

  private void validateUserPassword(User user, String password) {
    if (user.isNotMatchPassword(passwordEncoder, password)) {
      throw new PasswordNotMatchedException("비밀번호가 일치하지 않습니다.");
    }
  }
}
