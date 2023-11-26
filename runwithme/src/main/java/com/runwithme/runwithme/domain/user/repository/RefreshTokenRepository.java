package com.runwithme.runwithme.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.runwithme.runwithme.domain.user.entity.RefreshToken;
import com.runwithme.runwithme.domain.user.entity.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	Optional<RefreshToken> findByUser(User user);

	Optional<RefreshToken> findByName(String name);
}
