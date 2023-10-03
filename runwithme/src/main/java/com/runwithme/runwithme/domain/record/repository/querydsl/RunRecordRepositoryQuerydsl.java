package com.runwithme.runwithme.domain.record.repository.querydsl;

import java.util.List;

import com.runwithme.runwithme.domain.record.dto.CoordinateDto;
import com.runwithme.runwithme.domain.record.dto.RecordWeeklyCountDto;

public interface RunRecordRepositoryQuerydsl {
    int[] coordinatesInsertBatch(Long recordSeq, List<CoordinateDto> coordinates);
	List<RecordWeeklyCountDto> getWeeklySuccessYCount(Long challengeSeq);
}
