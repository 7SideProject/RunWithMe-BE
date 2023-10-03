package com.runwithme.runwithme.domain.challenge.repository.querydsl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.runwithme.runwithme.domain.challenge.dto.ChallengeBoardResponseDto;

public interface ChallengeBoardRepositoryQuerydsl {
    Page<ChallengeBoardResponseDto> findAllBoardPage(Long cursorSeq, Long userSeq, Long challengeSeq, Pageable pageable);
}
