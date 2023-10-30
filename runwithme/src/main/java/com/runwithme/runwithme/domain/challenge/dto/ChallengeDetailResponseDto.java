package com.runwithme.runwithme.domain.challenge.dto;

import java.time.LocalDate;

import com.querydsl.core.annotations.QueryProjection;
import com.runwithme.runwithme.domain.challenge.entity.GoalType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeDetailResponseDto {
    private Long seq;
    private Long managerSeq;
    private Long image;
    private String managerName;
    private String description;
    private String name;
    private Long goalDays;
    private GoalType goalType;
    private Long goalAmount;
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private Long nowMember;
    private Long maxMember;
    private Long cost;
    private char deleteYn;
    private boolean challengeUserFlag;
    private boolean canRunning;

    @QueryProjection
    public ChallengeDetailResponseDto(Long seq, Long managerSeq, Long image, String managerName, String description, String name, Long goalDays, GoalType goalType, Long goalAmount, LocalDate dateStart, LocalDate dateEnd, Long nowMember, Long maxMember, Long cost, char deleteYn, boolean challengeUserFlag, boolean canRunning) {
        this.seq = seq;
        this.managerSeq = managerSeq;
        this.image = image;
        this.managerName = managerName;
        this.description = description;
        this.name = name;
        this.goalDays = goalDays;
        this.goalType = goalType;
        this.goalAmount = goalAmount;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.nowMember = nowMember;
        this.maxMember = maxMember;
        this.cost = cost;
        this.deleteYn = deleteYn;
        this.challengeUserFlag = challengeUserFlag;
        this.canRunning = canRunning;
    }
}
