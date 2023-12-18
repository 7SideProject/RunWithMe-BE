package com.runwithme.runwithme.domain.challenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.runwithme.runwithme.domain.challenge.entity.ChallengeUser;
import com.runwithme.runwithme.domain.challenge.repository.querydsl.ChallengeUserRepositoryQuerydsl;

public interface ChallengeUserRepository extends JpaRepository<ChallengeUser, Long>, ChallengeUserRepositoryQuerydsl {
	boolean existsByUserSeqAndChallengeSeq(Long userSeq, Long challengeSeq);
	void deleteByUserSeqAndChallengeSeq(Long userSeq, Long challengeSeq);

}
