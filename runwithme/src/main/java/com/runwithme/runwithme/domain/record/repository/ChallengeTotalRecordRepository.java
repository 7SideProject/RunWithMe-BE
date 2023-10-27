package com.runwithme.runwithme.domain.record.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.runwithme.runwithme.domain.record.entity.ChallengeTotalRecord;

public interface ChallengeTotalRecordRepository extends JpaRepository<ChallengeTotalRecord, Long> {
    ChallengeTotalRecord findByUserSeqAndChallengeSeq(Long userSeq, Long challengeSeq);

    List<ChallengeTotalRecord> findByUserSeq(Long userSeq);
}
