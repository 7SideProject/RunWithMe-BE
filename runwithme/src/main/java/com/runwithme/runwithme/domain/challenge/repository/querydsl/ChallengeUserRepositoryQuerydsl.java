package com.runwithme.runwithme.domain.challenge.repository.querydsl;

import java.util.List;

import com.runwithme.runwithme.domain.challenge.dto.ChallengeUserDto;

public interface ChallengeUserRepositoryQuerydsl {
	List<ChallengeUserDto> findChallengeUserListByChallengeSeq(Long challengeSeq);
}
