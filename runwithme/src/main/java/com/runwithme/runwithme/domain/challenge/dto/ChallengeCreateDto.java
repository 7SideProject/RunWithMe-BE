package com.runwithme.runwithme.domain.challenge.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeCreateDto {

    private String name;
    private String description;
    private Long goalDays;
    private String goalType;
    private Long goalAmount;

    private LocalDateTime timeStart;
    private LocalDateTime timeEnd;

    private String password;
    private Long cost;

    private Long nowMember;
    private Long maxMember;

}
