package com.sparta.preonboarding.domain.user.entity;

import lombok.Getter;

@Getter
public enum UserRole {
    USER("ROLE_USER");

    private final String authority;

    UserRole(String authority) {
        this.authority = authority;
    }
}
