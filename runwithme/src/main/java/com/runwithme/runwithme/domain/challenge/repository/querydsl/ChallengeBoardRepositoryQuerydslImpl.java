package com.runwithme.runwithme.domain.challenge.repository.querydsl;

import com.querydsl.core.QueryResults;
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
    public Page<ChallengeBoardResponseDto> findAllBoardPage(Long challengeSeq, Pageable pageable) {
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
                )
                .orderBy(challengeBoard.seq.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }
}
