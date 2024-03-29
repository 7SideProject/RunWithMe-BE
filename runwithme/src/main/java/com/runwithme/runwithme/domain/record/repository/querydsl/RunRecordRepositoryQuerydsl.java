package com.runwithme.runwithme.domain.record.repository.querydsl;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.runwithme.runwithme.domain.record.dto.CoordinateDto;
import com.runwithme.runwithme.domain.record.dto.RecordWeeklyCountDto;
import com.runwithme.runwithme.domain.record.dto.RunRecordDetailDto;
import com.runwithme.runwithme.domain.record.dto.RunRecordResponseDto;
import com.runwithme.runwithme.domain.record.entity.RunRecord;

public interface RunRecordRepositoryQuerydsl {
	int[] coordinatesInsertBatch(RunRecord runRecord, List<CoordinateDto> coordinates);

	Page<RunRecordResponseDto> findAllRecordPage(Long cursorSeq, Long challengeSeq, Pageable pageable);

	List<RunRecordResponseDto> findAllMyRecord(Long userSeq, Long challengeSeq);

	Optional<RunRecordDetailDto> findRunRecordDetail(Long recordSeq);

	List<RecordWeeklyCountDto> getWeeklySuccessYCount(Long challengeSeq);

	List<CoordinateDto> findCoordinate(Long recordSeq);
}
