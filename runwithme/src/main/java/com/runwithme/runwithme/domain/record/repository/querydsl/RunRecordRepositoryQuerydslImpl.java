package com.runwithme.runwithme.domain.record.repository.querydsl;

import static com.runwithme.runwithme.domain.challenge.entity.QChallenge.*;
import static com.runwithme.runwithme.domain.record.entity.QRunRecord.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.runwithme.runwithme.domain.record.dto.CoordinateDto;
import com.runwithme.runwithme.domain.record.dto.QRecordWeeklyCountDto;
import com.runwithme.runwithme.domain.record.dto.QRunRecordResponseDto;
import com.runwithme.runwithme.domain.record.dto.RecordWeeklyCountDto;
import com.runwithme.runwithme.domain.record.dto.RunRecordResponseDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RunRecordRepositoryQuerydslImpl implements RunRecordRepositoryQuerydsl {
	private final JdbcTemplate jdbcTemplate;
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public int[] coordinatesInsertBatch(Long recordSeq, List<CoordinateDto> coordinates) {
		return jdbcTemplate.batchUpdate(
			"insert into t_record_coordinate(seq, latitude, longitude) " + "values(?, ?, ?)",
			new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setLong(1, recordSeq);
					ps.setDouble(2, coordinates.get(i).getLatitude());
					ps.setDouble(3, coordinates.get(i).getLongitude());
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
			.where(runRecord.challengeSeq.eq(challengeSeq), eqCursorSeq(cursorSeq))
			.orderBy(runRecord.seq.desc())
			.limit(pageable.getPageSize())
			.fetchResults();
		return new PageImpl<>(result.getResults(), pageable, result.getTotal());
	}

	@Override
	public List<RecordWeeklyCountDto> getWeeklySuccessYCount(Long challengeSeq) {
		return jpaQueryFactory
			.select(new QRecordWeeklyCountDto(
				runRecord.user.seq,
				runRecord.weekly,
				runRecord.count()))
			.from(runRecord)
			.where(runRecord.challengeSeq.eq(challengeSeq)
				.and(runRecord.successYn.eq('Y')))
			.groupBy(runRecord.user.seq, runRecord.weekly)
			.orderBy(runRecord.user.seq.asc())
			.fetch();
	}

	private BooleanExpression eqCursorSeq(Long cursorSeq) {
		if (cursorSeq == null) return null;
		return cursorSeq == 0 ? challenge.seq.gt(cursorSeq) : challenge.seq.lt(cursorSeq);
	}
}
