package com.runwithme.runwithme.domain.challenge.repository.querydsl;

import static com.runwithme.runwithme.domain.challenge.entity.QChallenge.*;
import static com.runwithme.runwithme.domain.challenge.entity.QChallengeBoard.*;
import static com.runwithme.runwithme.domain.challenge.entity.QChallengeBoardWarn.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.runwithme.runwithme.domain.challenge.dto.ChallengeBoardResponseDto;
import com.runwithme.runwithme.domain.challenge.dto.QChallengeBoardResponseDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChallengeBoardRepositoryQuerydslImpl implements ChallengeBoardRepositoryQuerydsl {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<ChallengeBoardResponseDto> findAllBoardPage(Long cursorSeq, Long userSeq, Long challengeSeq, Pageable pageable) {
		QueryResults<ChallengeBoardResponseDto> results = jpaQueryFactory
			.select(new QChallengeBoardResponseDto(
					challengeBoard.seq,
					challengeBoard.user.seq,
					challengeBoard.user.nickname,
					challengeBoard.image.seq,
					challengeBoard.challengeBoardContent,
					isImage()
				)
			).from(challengeBoard)
			.where(challengeBoard.challengeSeq
					.eq(challengeSeq)
					.and(challengeBoard.seq.notIn(getWarnBoardByUserSeq(userSeq)))
				, eqCursorSeq(cursorSeq))
			.orderBy(challengeBoard.seq.desc())
			.limit(pageable.getPageSize())
			.fetchResults();

		return new PageImpl<>(results.getResults(), pageable, results.getTotal());
	}

	public BooleanExpression isImage() {
		if (challengeBoard == null) return null;
		return challengeBoard.image.isNotNull();
	}

	private BooleanExpression eqCursorSeq(Long cursorSeq) {
		if (cursorSeq == null) return null;
		return cursorSeq == 0 ? challengeBoard.seq.gt(cursorSeq) : challengeBoard.seq.lt(cursorSeq);
	}

	private JPQLQuery<Long> getWarnBoardByUserSeq(Long userSeq) {
		return JPAExpressions
			.select(challengeBoardWarn.challengeBoard.seq)
			.where(challengeBoardWarn.user.seq.eq(userSeq))
			.from(challengeBoardWarn);
	}
}
