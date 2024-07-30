package com.sparta.preonboarding.domain.user.repository;

import backend.preonboarding.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
