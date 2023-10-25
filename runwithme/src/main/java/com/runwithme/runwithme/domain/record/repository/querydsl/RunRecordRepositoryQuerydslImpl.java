package com.runwithme.runwithme.domain.record.repository.querydsl;

import static com.runwithme.runwithme.domain.record.entity.QRunRecord.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.runwithme.runwithme.domain.record.dto.CoordinateDto;
import com.runwithme.runwithme.domain.record.dto.QRecordWeeklyCountDto;
import com.runwithme.runwithme.domain.record.dto.RecordWeeklyCountDto;

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
	public List<RecordWeeklyCountDto> getWeeklySuccessYCount(Long challengeSeq) {
		return jpaQueryFactory
			.select(new QRecordWeeklyCountDto(
				runRecord.userSeq,
				runRecord.weekly,
				runRecord.count()))
			.from(runRecord)
			.where(runRecord.challengeSeq.eq(challengeSeq)
				.and(runRecord.successYN))
			.groupBy(runRecord.userSeq, runRecord.weekly)
			.orderBy(runRecord.userSeq.asc())
			.fetch();
	}
}
