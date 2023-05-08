package com.runwithme.runwithme.domain.challenge.repository.querydsl;

import com.runwithme.runwithme.domain.challenge.dto.ChallengeResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChallengeRepositoryQuerydsl {

    Page<ChallengeResponseDto> findAllChallengePage(Long userSeq, Pageable pageable);
    Page<ChallengeResponseDto> findMyChallengePage(Long userSeq, Pageable pageable);
}
