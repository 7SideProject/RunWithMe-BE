package com.runwithme.runwithme.domain.challenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.runwithme.runwithme.domain.challenge.entity.ChallengeUser;

public interface ChallengeUserRepository extends JpaRepository<ChallengeUser, Long> {
	boolean existsByUserSeqAndChallengeSeq(Long userSeq, Long challengeSeq);
}
