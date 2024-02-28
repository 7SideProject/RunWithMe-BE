package com.runwithme.runwithme.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.runwithme.runwithme.domain.user.entity.ProviderType;
import com.runwithme.runwithme.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);

	boolean existsByNickname(String nickname);

	Optional<User> findBySeq(Long seq);

	Optional<User> findByProviderTypeAndResourceId(ProviderType providerType, String resourceId);
}
