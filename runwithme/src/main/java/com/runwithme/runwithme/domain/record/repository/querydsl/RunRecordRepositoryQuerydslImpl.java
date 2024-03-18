package com.runwithme.runwithme.domain.record.repository.querydsl;

import static com.runwithme.runwithme.domain.challenge.entity.QChallenge.*;
import static com.runwithme.runwithme.domain.record.entity.QRecordCoordinate.*;
import static com.runwithme.runwithme.domain.record.entity.QRunRecord.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.runwithme.runwithme.domain.record.dto.*;
import com.runwithme.runwithme.domain.record.entity.RunRecord;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RunRecordRepositoryQuerydslImpl implements RunRecordRepositoryQuerydsl {
	private final JdbcTemplate jdbcTemplate;
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public int[] coordinatesInsertBatch(RunRecord runRecord, List<CoordinateDto> coordinates) {
		return jdbcTemplate.batchUpdate(
			"insert into t_record_coordinate(latitude, longitude, run_record_seq) " + "values(?, ?, ?)",
			new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setInt(1, coordinates.get(i).getLatitude());
					ps.setInt(2, coordinates.get(i).getLongitude());
					ps.setLong(3, runRecord.getSeq());
				}

				@Override
				public int getBatchSize() {
					return coordinates.size();
				}
			}
		);
	}

	@Override
	public Page<RunRecordResponseDto> findAllRecordPage(Long cursorSeq, Long challengeSeq, Pageable pageable) {
		QueryResults<RunRecordResponseDto> result = jpaQueryFactory
			.select(new QRunRecordResponseDto(
				runRecord.seq,
				runRecord.challenge.name,
				runRecord.image.seq,
				runRecord.user.seq,
				runRecord.user.nickname,
				runRecord.runningDay,
				runRecord.startTime,
				runRecord.endTime,
				runRecord.runningDistance,
				runRecord.runningTime,
				runRecord.calorie,
				runRecord.avgSpeed,
				runRecord.successYn
			))
			.from(runRecord)
			.where(runRecord.challenge.seq.eq(challengeSeq), eqCursorSeq(cursorSeq))
			.orderBy(runRecord.seq.desc())
			.limit(pageable.getPageSize())
			.fetchResults();
		return new PageImpl<>(result.getResults(), pageable, result.getTotal());
	}

	@Override
	public List<RunRecordResponseDto> findAllMyRecord(Long userSeq, Long challengeSeq) {
		return jpaQueryFactory
			.select(new QRunRecordResponseDto(
				runRecord.seq,
				runRecord.challenge.name,
				runRecord.image.seq,
				runRecord.user.seq,
				runRecord.user.nickname,
				runRecord.runningDay,
				runRecord.startTime,
				runRecord.endTime,
				runRecord.runningDistance,
				runRecord.runningTime,
				runRecord.calorie,
				runRecord.avgSpeed,
				runRecord.successYn
			))
			.from(runRecord)
			.where(runRecord.user.seq.eq(userSeq).and(runRecord.challenge.seq.eq(challengeSeq)))
			.orderBy(runRecord.seq.desc())
			.fetch();
	}

	@Override
	public Optional<RunRecordDetailDto> findRunRecordDetail(Long recordSeq) {
		return Optional.ofNullable(jpaQueryFactory
			.select(new QRunRecordDetailDto(
				runRecord.seq,
				runRecord.image.seq,
				runRecord.user.seq,
				runRecord.user.nickname,
				runRecord.runningDay,
				runRecord.startTime,
				runRecord.runningDistance,
				runRecord.runningTime,
				runRecord.calorie,
				runRecord.avgSpeed
			))
			.from(runRecord)
			.where(runRecord.seq.eq(recordSeq))
			.fetchOne());
	}

	@Override
	public List<RecordWeeklyCountDto> getWeeklySuccessYCount(Long challengeSeq) {
		return jpaQueryFactory
			.select(new QRecordWeeklyCountDto(
				runRecord.user.seq,
				runRecord.weekly,
				runRecord.count()))
			.from(runRecord)
			.where(runRecord.challenge.seq.eq(challengeSeq)
				.and(runRecord.successYn.eq('Y')))
			.groupBy(runRecord.user.seq, runRecord.weekly)
			.orderBy(runRecord.user.seq.asc())
			.fetch();
	}

	private BooleanExpression eqCursorSeq(Long cursorSeq) {
		if (cursorSeq == null) return null;
		return cursorSeq == 0 ? challenge.seq.gt(cursorSeq) : challenge.seq.lt(cursorSeq);
	}

	@Override
	public List<CoordinateDto> findCoordinate(Long recordSeq) {
		return jpaQueryFactory
			.select(new QCoordinateDto(
				recordCoordinate.latitude,
				recordCoordinate.longitude
			))
			.from(recordCoordinate)
			.where(recordCoordinate.runRecord.seq.eq(recordSeq))
			.orderBy(recordCoordinate.seq.desc())
			.fetch();
	}
}
