package com.sparta.preonboarding.global.exception.custom;

public class PasswordNotMatchedException extends RuntimeException {

  public PasswordNotMatchedException(String message) {
    super(message);
  }
}
