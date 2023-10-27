package com.runwithme.runwithme.domain.challenge.repository.querydsl;

import static com.runwithme.runwithme.domain.challenge.entity.QChallenge.*;
import static com.runwithme.runwithme.domain.challenge.entity.QChallengeUser.*;
import static com.runwithme.runwithme.domain.record.entity.QRunRecord.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.runwithme.runwithme.domain.challenge.dto.ChallengeEndDto;
import com.runwithme.runwithme.domain.challenge.dto.ChallengeResponseDto;
import com.runwithme.runwithme.domain.challenge.dto.QChallengeEndDto;
import com.runwithme.runwithme.domain.challenge.dto.QChallengeResponseDto;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class ChallengeRepositoryQuerydslImpl implements ChallengeRepositoryQuerydsl {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Optional<ChallengeResponseDto> findChallengeBySeq(Long userSeq, Long challengeSeq) {
		return Optional.ofNullable(jpaQueryFactory
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
			.where(challenge.seq.eq(challengeSeq))
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
			.where(eqCursorSeq(cursorSeq))
			.orderBy(challenge.seq.desc())
			.limit(pageable.getPageSize())
			.fetchResults();
		return new PageImpl<>(result.getResults(), pageable, result.getTotal());
	}

	@Override
	public Page<ChallengeResponseDto> findRecruitChallengePage(Long cursorSeq, Long userSeq, LocalDate nowTime, Pageable pageable) {
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
			.where(challenge.dateStart.after(nowTime),
				eqCursorSeq(cursorSeq))
			.orderBy(challenge.seq.desc())
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
			.where(challengeUser.user.seq.eq(userSeq))
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

	private BooleanExpression eqCursorSeq(Long cursorSeq) {
		if (cursorSeq == null) return null;
		return cursorSeq == 0 ? challenge.seq.gt(cursorSeq) : challenge.seq.lt(cursorSeq);
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
