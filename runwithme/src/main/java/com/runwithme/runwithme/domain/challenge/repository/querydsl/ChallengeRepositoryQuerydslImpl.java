package com.runwithme.runwithme.domain.challenge.repository.querydsl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.runwithme.runwithme.domain.challenge.dto.ChallengeEndDto;

import lombok.RequiredArgsConstructor;

import com.runwithme.runwithme.domain.challenge.dto.ChallengeResponseDto;
import com.runwithme.runwithme.domain.challenge.dto.QChallengeEndDto;
import com.runwithme.runwithme.domain.challenge.dto.QChallengeResponseDto;

@RequiredArgsConstructor
public class ChallengeRepositoryQuerydslImpl implements ChallengeRepositoryQuerydsl{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<ChallengeResponseDto> findChallengeBySeq(Long userSeq, Long challengeSeq){
        return Optional.ofNullable(jpaQueryFactory
                .select(new QChallengeResponseDto(
                        challenge.seq,
                        challenge.manager.seq,
                        challenge.manager.nickname,
                        challenge.description,
                        challenge.image.seq,
                        challenge.name,
                        challenge.goalDays,
                        challenge.goalType,
                        challenge.goalAmount,
                        challenge.dateStart,
                        challenge.dateEnd,
                        challenge.nowMember,
                        challenge.maxMember,
                        challenge.cost,
                        isExistChallengeUser(userSeq)
                ))
                .from(challenge)
                .leftJoin(challengeUser)
                .on(challenge.seq.eq(challengeUser.challenge.seq).and(challengeUser.user.seq.eq(userSeq)))
                .where(challenge.seq.eq(challengeSeq))
                .fetchOne());
    }
    @Override
    public Page<ChallengeResponseDto> findAllChallengePage(Long cursorSeq, Long userSeq, Pageable pageable){
        QueryResults<ChallengeResponseDto> result = jpaQueryFactory
                .select(new QChallengeResponseDto(
                        challenge.seq,
                        challenge.manager.seq,
                        challenge.manager.nickname,
                        challenge.description,
                        challenge.image.seq,
                        challenge.name,
                        challenge.goalDays,
                        challenge.goalType,
                        challenge.goalAmount,
                        challenge.dateStart,
                        challenge.dateEnd,
                        challenge.nowMember,
                        challenge.maxMember,
                        challenge.cost,
                        isExistChallengeUser(userSeq)
                ))
                .from(challenge)
                .leftJoin(challengeUser)
                .on(challenge.seq.eq(challengeUser.challenge.seq).and(challengeUser.user.seq.eq(userSeq)))
                .where(eqCursorSeq(cursorSeq))
                .orderBy(challenge.seq.desc())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }
    @Override
    public Page<ChallengeResponseDto> findRecruitChallengePage(Long cursorSeq, Long userSeq, LocalDate nowTime, Pageable pageable){
        QueryResults<ChallengeResponseDto> result = jpaQueryFactory
                .select(new QChallengeResponseDto(
                            challenge.seq,
                            challenge.manager.seq,
                            challenge.manager.nickname,
                            challenge.description,
                            challenge.image.seq,
                            challenge.name,
                            challenge.goalDays,
                            challenge.goalType,
                            challenge.goalAmount,
                            challenge.dateStart,
                            challenge.dateEnd,
                            challenge.nowMember,
                            challenge.maxMember,
                            challenge.cost,
                            isExistChallengeUser(userSeq)
                ))
                .from(challenge)
                .leftJoin(challengeUser)
                .on(challenge.seq.eq(challengeUser.challenge.seq).and(challengeUser.user.seq.eq(userSeq)))
                .where(challenge.dateStart.after(nowTime),
                        eqCursorSeq(cursorSeq))
                .orderBy(challenge.seq.desc())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }
    @Override
    public Page<ChallengeResponseDto> findMyChallengePage(Long cursorSeq, Long userSeq, Pageable pageable){
        QueryResults<ChallengeResponseDto> result = jpaQueryFactory
                .select(new QChallengeResponseDto(
                        challengeUser.challenge.seq,
                        challengeUser.challenge.manager.seq,
                        challengeUser.challenge.manager.nickname,
                        challengeUser.challenge.description,
                        challengeUser.challenge.image.seq,
                        challengeUser.challenge.name,
                        challengeUser.challenge.goalDays,
                        challengeUser.challenge.goalType,
                        challengeUser.challenge.goalAmount,
                        challengeUser.challenge.dateStart,
                        challengeUser.challenge.dateEnd,
                        challengeUser.challenge.nowMember,
                        challengeUser.challenge.maxMember,
                        challengeUser.challenge.cost,
                        isExistChallengeUser(userSeq)
                ))
                .from(challengeUser)
                .where(challengeUser.user.seq.eq(userSeq))
                .orderBy(challengeUser.challenge.seq.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }
    private BooleanExpression isExistChallengeUserWhereChallengeEqAndUserEq(Long userSeq){
        return JPAExpressions
                .selectFrom(challengeUser)
                .where(challengeUser.challenge.seq.eq(challenge.seq).and(challengeUser.user.seq.eq(userSeq)))
                .exists();
    }
    public BooleanExpression isExistChallengeUser(Long userSeq){
        if (userSeq == null) return null;

	private BooleanExpression eqCursorSeq(Long cursorSeq) {
		return cursorSeq == null ? null : challenge.seq.gt(cursorSeq);
	}

	@Override
	public List<ChallengeEndDto> findByDateEndIsToday(LocalDate today) {
		return jpaQueryFactory
			.select(new QChallengeEndDto(
				challenge.seq,
				challenge.manager.seq,
				challenge.goalDays,
				challenge.cost,
				challenge.dateEnd
			))
			.from(challenge)
			.where(challenge.dateEnd.eq(today))
			.fetch();
	}

	@Override
	public int getChallengeCount(Long challengeSeq) {
		return (int) jpaQueryFactory
			.select()
			.from(challenge)
			.where(challenge.seq.eq(challengeSeq))
			.fetchCount();
	}

	public void deleteMyChallenge(Long challengeSeq){
		return;
	}
        return challengeUser.user.seq.isNotNull();
    }
    private BooleanExpression eqCursorSeq(Long cursorSeq) {
        if (cursorSeq == null) return null;
        return cursorSeq == 0 ? challenge.seq.gt(cursorSeq) : challenge.seq.lt(cursorSeq);
    }
}
