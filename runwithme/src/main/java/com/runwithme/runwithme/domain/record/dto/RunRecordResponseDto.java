package com.runwithme.runwithme.domain.record.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RunRecordResponseDto {
	private Long seq;
	private String challengeName;
	private Long image;
	private Long userSeq;
	private String nickname;
	private String runningDay;
	private String startTime;
	private String endTime;
	private Long runningDistance;
	private Long runningTime;
	private Long calorie;
	private double avgSpeed;
	private char successYn;

	@QueryProjection
	public RunRecordResponseDto(Long seq, String challengeName, Long image, Long userSeq, String nickname, String runningDay, String startTime, String endTime, Long runningDistance, Long runningTime, Long calorie, double avgSpeed, char successYn) {
		this.seq = seq;
		this.challengeName = challengeName;
		this.image = image;
		this.userSeq = userSeq;
		this.nickname = nickname;
		this.runningDay = runningDay;
		this.startTime = startTime;
		this.endTime = endTime;
		this.runningDistance = runningDistance;
		this.runningTime = runningTime;
		this.calorie = calorie;
		this.avgSpeed = avgSpeed;
		this.successYn = successYn;
	}
}
