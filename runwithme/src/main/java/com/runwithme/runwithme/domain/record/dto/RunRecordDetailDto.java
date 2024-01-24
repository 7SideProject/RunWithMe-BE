package com.runwithme.runwithme.domain.record.dto;

import java.util.List;

import com.querydsl.core.annotations.QueryProjection;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RunRecordDetailDto {
	private Long seq;
	private Long image;
	private Long userSeq;
	private String nickname;
	private String runningDay;
	private String startTime;
	private Long runningDistance;
	private Long runningTime;
	private Long calorie;
	private double avgSpeed;
	private List<CoordinateDto> coordinates;

	@QueryProjection
	public RunRecordDetailDto(Long seq, Long image, Long userSeq, String nickname, String runningDay, String startTime, Long runningDistance, Long runningTime, Long calorie, double avgSpeed) {
		this.seq = seq;
		this.image = image;
		this.userSeq = userSeq;
		this.nickname = nickname;
		this.runningDay = runningDay;
		this.startTime = startTime;
		this.runningDistance = runningDistance;
		this.runningTime = runningTime;
		this.calorie = calorie;
		this.avgSpeed = avgSpeed;
	}
}
