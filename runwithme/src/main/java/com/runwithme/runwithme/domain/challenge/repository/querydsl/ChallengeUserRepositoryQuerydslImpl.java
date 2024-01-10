package com.runwithme.runwithme.domain.challenge.repository.querydsl;

import static com.runwithme.runwithme.domain.challenge.entity.QChallengeUser.*;

import java.util.List;

import com.querydsl.core.QueryResults;
import com.runwithme.runwithme.domain.challenge.dto.ChallengeUserDto;
import com.runwithme.runwithme.domain.challenge.dto.QChallengeUserDto;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChallengeUserRepositoryQuerydslImpl implements ChallengeUserRepositoryQuerydsl{

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<ChallengeUserDto> findChallengeUserListByChallengeSeq(Long challengeSeq) {
		QueryResults<ChallengeUserDto> result = jpaQueryFactory
			.select(new QChallengeUserDto(
				challengeUser.seq,
				challengeUser.user,
				challengeUser.challenge.cost
			))
			.from(challengeUser)
			.where(challengeUser.challenge.seq.eq(challengeSeq))
			.orderBy(challengeUser.seq.desc())
			.fetchResults();
		return result.getResults();
	}
}
