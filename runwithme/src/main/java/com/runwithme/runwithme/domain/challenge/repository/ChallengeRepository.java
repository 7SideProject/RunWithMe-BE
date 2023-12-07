package com.runwithme.runwithme.domain.challenge.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.runwithme.runwithme.domain.challenge.dto.ChallengeResponseDto;
import com.runwithme.runwithme.domain.challenge.entity.Challenge;
import com.runwithme.runwithme.domain.challenge.repository.querydsl.ChallengeRepositoryQuerydsl;

public interface ChallengeRepository extends JpaRepository<Challenge, Long>, ChallengeRepositoryQuerydsl {

}
