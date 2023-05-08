package com.runwithme.runwithme.domain.challenge.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeResponseDto {
    private Long seq;
    private Long managerSeq;
    private String name;
    private Long imgSeq;
    private Long goalDays;
    private String goalType;
    private Long goalAmount;
    private LocalDateTime timeStart;
    private LocalDateTime timeEnd;
//    private boolean joinUserFlag;

    @QueryProjection
    public ChallengeResponseDto(Long seq, Long managerSeq, String name, Long imgSeq, Long goalDays, String goalType, Long goalAmount, LocalDateTime timeStart, LocalDateTime timeEnd) {
        this.seq = seq;
        this.managerSeq = managerSeq;
        this.name = name;
        this.imgSeq = imgSeq;
        this.goalDays = goalDays;
        this.goalType = goalType;
        this.goalAmount = goalAmount;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }
}
