package com.runwithme.runwithme.domain.challenge.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.runwithme.runwithme.domain.challenge.dto.ChallengeResponseDto;
import com.runwithme.runwithme.domain.challenge.entity.Challenge;
import com.runwithme.runwithme.domain.challenge.repository.querydsl.ChallengeRepositoryQuerydsl;

public interface ChallengeRepository extends JpaRepository<Challenge, Long>, ChallengeRepositoryQuerydsl {
	@Query("""
		SELECT new com.runwithme.runwithme.domain.challenge.dto.ChallengeResponseDto(
		c1_0.seq, c1_0.manager.seq, c1_0.manager.nickname, c1_0.image.seq, c1_0.name, c1_0.goalDays, c1_0.goalType, c1_0.goalAmount, c1_0.dateStart, c1_0.dateEnd, c1_0.maxMember, c1_0.cost, 
		case cu.user.seq when :userSeq then true else false end)
		FROM Challenge c1_0 
		JOIN User m1_0 
		ON m1_0.seq=c1_0.manager.seq 
		LEFT JOIN ChallengeUser cu
		ON c1_0.seq = cu.challenge.seq
		WHERE c1_0.seq=:challengeSeq AND cu.user.seq=:userSeq
		""")
	Optional<ChallengeResponseDto> findChallengeBySeq(@Param("userSeq") Long userSeq, @Param("challengeSeq") Long challengeSeq);
}
