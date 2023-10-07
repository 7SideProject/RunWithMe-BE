package com.runwithme.runwithme.domain.challenge.dto;

import java.time.LocalDate;

import com.querydsl.core.annotations.QueryProjection;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeResponseDto {
    private Long seq;
    private Long managerSeq;
    private String managerName;
    private Long image;
    private String name;
    private Long goalDays;
    private String goalType;
    private Long goalAmount;
    private LocalDate timeStart;
    private LocalDate timeEnd;
    private Long nowMember;
    private Long maxMember;
    private Long cost;
    private boolean challengeUserFlag;

    @QueryProjection
    public ChallengeResponseDto(Long seq, Long managerSeq, String managerName, Long image, String name, Long goalDays, String goalType, Long goalAmount, LocalDate timeStart, LocalDate timeEnd, Long nowMember, Long maxMember, Long cost, boolean challengeUserFlag) {
        this.seq = seq;
        this.managerSeq = managerSeq;
        this.managerName = managerName;
        this.image = image;
        this.name = name;
        this.goalDays = goalDays;
        this.goalType = goalType;
        this.goalAmount = goalAmount;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.nowMember = nowMember;
        this.maxMember = maxMember;
        this.cost = cost;
        this.challengeUserFlag = challengeUserFlag;
    }
}
