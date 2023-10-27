package com.runwithme.runwithme.domain.record.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RunRecordPostDto {
    private String startTime;
    private String endTime;
    private Long runningTime;
    private Long runningDistance;
    private List coordinates;
    private char successYn;
}
