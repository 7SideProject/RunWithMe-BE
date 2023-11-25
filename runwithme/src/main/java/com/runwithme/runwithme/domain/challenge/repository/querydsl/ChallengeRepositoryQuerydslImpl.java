package com.runwithme.runwithme.domain.challenge.repository.querydsl;

import static com.runwithme.runwithme.domain.challenge.entity.QChallenge.*;
import static com.runwithme.runwithme.domain.challenge.entity.QChallengeUser.*;
import static com.runwithme.runwithme.domain.record.entity.QRunRecord.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.querydsl.core.types.dsl.StringExpressions;
import com.runwithme.runwithme.domain.challenge.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;


import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class ChallengeRepositoryQuerydslImpl implements ChallengeRepositoryQuerydsl {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Optional<ChallengeDetailResponseDto> findChallengeBySeq(Long userSeq, Long challengeSeq, LocalDate nowTime) {
		return Optional.ofNullable(jpaQueryFactory
			.select(new QChallengeDetailResponseDto(
				challenge.seq,
				challenge.manager.seq,
				challenge.image.seq,
				challenge.manager.nickname,
				challenge.description,
				challenge.name,
				challenge.goalDays,
				challenge.goalType,
				challenge.goalAmount,
				challenge.dateStart,
				challenge.dateEnd,
				challenge.nowMember,
				challenge.maxMember,
				challenge.cost,
				challenge.deleteYn,
				isExistChallengeUser(userSeq),
				isExistRunRecord(userSeq)
			))
			.from(challenge)
			.leftJoin(challengeUser)
			.on(challenge.seq.eq(challengeUser.challenge.seq).and(challengeUser.user.seq.eq(userSeq)))
			.leftJoin(runRecord)
			.on(
					runRecord.challengeSeq.eq(challenge.seq)
							.and(runRecord.userSeq.eq(userSeq))
							.and(runRecord.regTime.eq(nowTime))
			)
			.where(challenge.seq.eq(challengeSeq).and(challenge.deleteYn.eq('N')))
			.fetchOne());
	}

	@Override
	public Page<ChallengeResponseDto> findAllChallengePage(Long cursorSeq, Long userSeq, Pageable pageable) {
		QueryResults<ChallengeResponseDto> result = jpaQueryFactory
			.select(new QChallengeResponseDto(
				challenge.seq,
				challenge.manager.seq,
				challenge.image.seq,
				challenge.manager.nickname,
				challenge.description,
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
			.where(challenge.deleteYn.eq('N'), eqCursorSeq(cursorSeq))
			.orderBy(challenge.seq.desc())
			.limit(pageable.getPageSize())
			.fetchResults();
		return new PageImpl<>(result.getResults(), pageable, result.getTotal());
	}

	@Override
	public Page<ChallengeResponseDto> findRecruitChallengePage(Long cursorSeq, String customCursor, Long userSeq, LocalDate nowTime, Pageable pageable) {
		QueryResults<ChallengeResponseDto> result = jpaQueryFactory
			.select(new QChallengeResponseDto(
				challenge.seq,
				challenge.manager.seq,
				challenge.image.seq,
				challenge.manager.nickname,
				challenge.description,
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
			.where(challenge.deleteYn.eq('N').and(challenge.dateStart.after(nowTime)),
				customCursor(cursorSeq, customCursor))
			.orderBy(challenge.dateStart.asc(), challenge.seq.asc())
			.limit(pageable.getPageSize())
			.fetchResults();
		return new PageImpl<>(result.getResults(), pageable, result.getTotal());
	}

	@Override
	public Page<ChallengeResponseDto> findMyChallengePage(Long cursorSeq, Long userSeq, Pageable pageable) {
		QueryResults<ChallengeResponseDto> result = jpaQueryFactory
			.select(new QChallengeResponseDto(
				challengeUser.challenge.seq,
				challengeUser.challenge.manager.seq,
				challengeUser.challenge.image.seq,
				challengeUser.challenge.manager.nickname,
				challengeUser.challenge.description,
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
			.where(challengeUser.user.seq.eq(userSeq).and(challengeUser.challenge.deleteYn.eq('N')))
			.orderBy(challengeUser.challenge.seq.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetchResults();
		return new PageImpl<>(result.getResults(), pageable, result.getTotal());
	}

	@Override
	public Page<ChallengeResponseDto> findMyRunningChallengePage(Long cursorSeq, Long userSeq, LocalDate nowTime, Pageable pageable){
		QueryResults<ChallengeResponseDto> result = jpaQueryFactory
				.select(new QChallengeResponseDto(
						challengeUser.challenge.seq,
						challengeUser.challenge.manager.seq,
						challengeUser.challenge.image.seq,
						challengeUser.challenge.manager.nickname,
						challengeUser.challenge.description,
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
				.leftJoin(runRecord)
				.on(
						runRecord.challengeSeq.eq(challengeUser.challenge.seq)
								.and(runRecord.userSeq.eq(userSeq))
								.and(runRecord.regTime.eq(nowTime))
				)
				.where(
						challengeUser.user.seq.eq(userSeq)
								.and(challengeUser.challenge.deleteYn.eq('N'))
								.and(challenge.dateStart.before(nowTime))
								.and(challenge.dateEnd.after(nowTime))
								.and(runRecord.seq.isNull())
						, eqCursorSeq(cursorSeq)
				)
				.orderBy(challengeUser.challenge.seq.desc())
				.limit(pageable.getPageSize())
				.fetchResults();
		return new PageImpl<>(result.getResults(), pageable, result.getTotal());
	}

	public BooleanExpression isExistChallengeUser(Long userSeq) {
		if (userSeq == null) return null;
		return challengeUser.user.seq.isNotNull();
	}

	public BooleanExpression isExistRunRecord(Long userSeq) {
		if (userSeq == null) return null;
		return runRecord.seq.isNull();
	}

	private BooleanExpression eqCursorSeq(Long cursorSeq) {
		if (cursorSeq == null) return null;
		return cursorSeq == 0 ? challenge.seq.gt(cursorSeq) : challenge.seq.lt(cursorSeq);
	}

	private BooleanExpression customCursor(Long cursorSeq, String customCursor) {
		if (cursorSeq == null) return null;
		return cursorSeq == 0 ? challenge.seq.gt(cursorSeq) :
			challenge.dateStart.stringValue().concat(StringExpressions.lpad(challenge.seq.stringValue(), 10, '0')).gt(customCursor);
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
}
