package com.runwithme.runwithme.domain.challenge.repository.querydsl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.runwithme.runwithme.domain.challenge.dto.ChallengeBoardResponseDto;
import com.runwithme.runwithme.domain.challenge.dto.QChallengeBoardResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static com.runwithme.runwithme.domain.challenge.entity.QChallengeBoard.challengeBoard;

@RequiredArgsConstructor
public class ChallengeBoardRepositoryQuerydslImpl implements ChallengeBoardRepositoryQuerydsl{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<ChallengeBoardResponseDto> findAllBoardPage(Long cursorSeq, Long challengeSeq, Pageable pageable) {
        QueryResults<ChallengeBoardResponseDto> results = jpaQueryFactory.select(new QChallengeBoardResponseDto(
                                challengeBoard.seq,
                                challengeBoard.user.seq,
                                challengeBoard.user.nickname,
                                challengeBoard.challengeSeq,
                                challengeBoard.challengeBoardContent
                        )
                ).from(challengeBoard)
                .where(
                        challengeBoard.challengeSeq.eq(challengeSeq)
                        , eqCursorSeq(cursorSeq))
                .orderBy(challengeBoard.seq.desc())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    private BooleanExpression eqCursorSeq(Long cursorSeq) {
        return cursorSeq == null ? null : challengeBoard.seq.lt(cursorSeq);
    }
}
