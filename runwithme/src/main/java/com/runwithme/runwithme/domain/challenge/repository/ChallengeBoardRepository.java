package com.runwithme.runwithme.domain.challenge.repository;

import com.runwithme.runwithme.domain.challenge.dto.ChallengeBoardResponseDto;
import com.runwithme.runwithme.domain.challenge.entity.ChallengeBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeBoardRepository extends JpaRepository<ChallengeBoard, Long> {

    Page<ChallengeBoardResponseDto> findAllByChallengeSeq(Long challengeSeq, Pageable pageable);
}
