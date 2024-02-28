package com.runwithme.runwithme.domain.challenge.repository.querydsl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.runwithme.runwithme.domain.challenge.dto.ChallengeDetailResponseDto;
import com.runwithme.runwithme.domain.challenge.dto.QChallengeDetailResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.runwithme.runwithme.domain.challenge.dto.ChallengeEndDto;
import com.runwithme.runwithme.domain.challenge.dto.ChallengeResponseDto;
import com.runwithme.runwithme.domain.user.entity.User;

public interface ChallengeRepositoryQuerydsl {

    Optional<ChallengeDetailResponseDto> findChallengeBySeq(Long userSeq, Long challengeSeq, LocalDate nowTime);

    Page<ChallengeResponseDto> findAllChallengePage(Long cursorSeq, Long userSeq, Pageable pageable);

	Page<ChallengeResponseDto> findRecruitChallengePage(Long cursorSeq, String customCursor, Long userSeq, LocalDate nowTime, Pageable pageable);

	Page<ChallengeResponseDto> findMyChallengePage(Long cursorSeq, Long userSeq, LocalDate nowTime, Pageable pageable);

	Page<ChallengeResponseDto> findMyRunningChallengePage(Long cursorSeq, Long userSeq, LocalDate nowTime, Pageable pageable);

	List<ChallengeEndDto> findByDateEndIsToday(LocalDate today);

	int getChallengeCount(Long challengeSeq);
}
