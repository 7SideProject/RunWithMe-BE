package com.runwithme.runwithme.domain.challenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.runwithme.runwithme.domain.challenge.entity.ChallengeBoard;
import com.runwithme.runwithme.domain.challenge.repository.querydsl.ChallengeBoardRepositoryQuerydsl;

public interface ChallengeBoardRepository extends JpaRepository<ChallengeBoard, Long>, ChallengeBoardRepositoryQuerydsl {

}
