package com.runwithme.runwithme.domain.challenge.dto;

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
public class ChallengeUserDto {
	private Long challengeSeq;
	private Long userSeq;

	@QueryProjection
	public ChallengeUserDto(Long challengeSeq, Long userSeq) {
		this.challengeSeq = challengeSeq;
		this.userSeq = userSeq;
	}
}
