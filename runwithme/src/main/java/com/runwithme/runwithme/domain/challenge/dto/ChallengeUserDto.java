package com.runwithme.runwithme.domain.challenge.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.runwithme.runwithme.domain.user.entity.User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeUserDto {
	private Long seq;
	private User user;
	private Long challengeCost;

	@QueryProjection
	public ChallengeUserDto(Long seq, User user, Long challengeCost) {
		this.seq = seq;
		this.user = user;
		this.challengeCost = challengeCost;
	}
}
