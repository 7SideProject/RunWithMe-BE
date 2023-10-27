package com.runwithme.runwithme.domain.challenge.repository.querydsl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.runwithme.runwithme.domain.challenge.dto.ChallengeEndDto;
import com.runwithme.runwithme.domain.challenge.dto.ChallengeResponseDto;

public interface ChallengeRepositoryQuerydsl {
	Optional<ChallengeResponseDto> findChallengeBySeq(Long userSeq, Long challengeSeq);

	Page<ChallengeResponseDto> findAllChallengePage(Long cursorSeq, Long userSeq, Pageable pageable);

	Page<ChallengeResponseDto> findRecruitChallengePage(Long cursorSeq, Long userSeq, LocalDate nowTime, Pageable pageable);

	Page<ChallengeResponseDto> findMyChallengePage(Long cursorSeq, Long userSeq, Pageable pageable);

	Page<ChallengeResponseDto> findMyRunningChallengePage(Long cursorSeq, Long userSeq, LocalDate nowTime, Pageable pageable);

	List<ChallengeEndDto> findByDateEndIsToday(LocalDate today);

	int getChallengeCount(Long challengeSeq);
}
