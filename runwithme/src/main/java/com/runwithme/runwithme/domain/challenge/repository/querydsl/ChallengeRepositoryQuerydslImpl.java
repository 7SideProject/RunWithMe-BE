package com.runwithme.runwithme.domain.challenge.repository.querydsl;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.runwithme.runwithme.domain.challenge.dto.ChallengeResponseDto;
import com.runwithme.runwithme.domain.challenge.dto.QChallengeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static com.runwithme.runwithme.domain.challenge.entity.QChallenge.challenge;
import static com.runwithme.runwithme.domain.challenge.entity.QChallengeUser.challengeUser;

@RequiredArgsConstructor
public class ChallengeRepositoryQuerydslImpl implements ChallengeRepositoryQuerydsl{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<ChallengeResponseDto> findAllChallengePage(Long userSeq, Pageable pageable){
        QueryResults<ChallengeResponseDto> result = jpaQueryFactory.select(new QChallengeResponseDto(
                        challenge.seq,
                        challenge.manager.seq,
                        challenge.manager.nickname,
                        challenge.image,
                        challenge.name,
                        challenge.goalDays,
                        challenge.goalType,
                        challenge.goalAmount,
                        challenge.timeStart,
                        challenge.timeEnd,
                        challenge.nowMember,
                        challenge.maxMember,
                        challenge.cost
                        )
                ).from(challenge)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public Page<ChallengeResponseDto> findMyChallengePage(Long userSeq, Pageable pageable){
        QueryResults<ChallengeResponseDto> result = jpaQueryFactory.select(new QChallengeResponseDto(
                        challengeUser.challenge.seq,
                        challengeUser.challenge.manager.seq,
                        challengeUser.challenge.manager.nickname,
                        challengeUser.challenge.image,
                        challengeUser.challenge.name,
                        challengeUser.challenge.goalDays,
                        challengeUser.challenge.goalType,
                        challengeUser.challenge.goalAmount,
                        challengeUser.challenge.timeStart,
                        challengeUser.challenge.timeEnd,
                        challengeUser.challenge.nowMember,
                        challengeUser.challenge.maxMember,
                        challengeUser.challenge.cost
                        )
                ).from(challengeUser)
                .where(challengeUser.userSeq.eq(userSeq))
                .orderBy(challengeUser.challenge.seq.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }
}
