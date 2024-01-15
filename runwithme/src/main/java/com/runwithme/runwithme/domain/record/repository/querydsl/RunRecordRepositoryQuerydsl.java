package com.runwithme.runwithme.domain.record.repository.querydsl;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.runwithme.runwithme.domain.record.dto.CoordinateDto;
import com.runwithme.runwithme.domain.record.dto.RecordWeeklyCountDto;
import com.runwithme.runwithme.domain.record.dto.RunRecordResponseDto;

public interface RunRecordRepositoryQuerydsl {
	int[] coordinatesInsertBatch(Long recordSeq, List<CoordinateDto> coordinates);

	Page<RunRecordResponseDto> findAllRecordPage(Long cursorSeq, Long challengeSeq, Pageable pageable);

	List<RecordWeeklyCountDto> getWeeklySuccessYCount(Long challengeSeq);
}
