package com.runwithme.runwithme.domain.challenge.repository.querydsl;

import com.runwithme.runwithme.domain.challenge.dto.ChallengeResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ChallengeRepositoryQuerydsl {

//    Optional<ChallengeResponseDto> findChallengeBySeq(Long userSeq, Long challengeSeq);

    Page<ChallengeResponseDto> findAllChallengePage(Long userSeq, Pageable pageable);

    Page<ChallengeResponseDto> findRecruitChallengePage(Long userSeq, LocalDateTime nowTime, Pageable pageable);

    Page<ChallengeResponseDto> findMyChallengePage(Long userSeq, Pageable pageable);

}
