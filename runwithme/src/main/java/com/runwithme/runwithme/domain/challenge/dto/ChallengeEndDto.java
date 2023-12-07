package com.runwithme.runwithme.domain.challenge.dto;

import java.time.LocalDate;

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
public class ChallengeEndDto {
	private Long seq;
	private Long managerSeq;
	private Long goalDays;
	private Long cost;
	private LocalDate dateEnd;

	@QueryProjection
	public ChallengeEndDto(Long seq, Long managerSeq, Long goalDays, Long cost, LocalDate dateEnd) {
		this.seq = seq;
		this.managerSeq = managerSeq;
		this.goalDays = goalDays;
		this.cost = cost;
		this.dateEnd = dateEnd;
	}
}
