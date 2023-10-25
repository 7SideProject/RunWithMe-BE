package com.runwithme.runwithme.domain.user.repository;

import org.springframework.data.repository.CrudRepository;

import com.runwithme.runwithme.domain.user.entity.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

}
