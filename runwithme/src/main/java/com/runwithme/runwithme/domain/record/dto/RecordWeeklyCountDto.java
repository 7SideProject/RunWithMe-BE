package com.runwithme.runwithme.domain.record.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecordWeeklyCountDto {
	private Long userSeq;
	private int weekly;
	private Long successCount;

	@QueryProjection
	public RecordWeeklyCountDto(Long userSeq, int weekly, Long successCount) {
		this.userSeq = userSeq;
		this.weekly = weekly;
		this.successCount = successCount;
	}
}
