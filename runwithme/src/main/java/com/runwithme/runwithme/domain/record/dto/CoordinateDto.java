package com.runwithme.runwithme.domain.record.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CoordinateDto {
    private int latitude;
    private int longitude;

    @QueryProjection
    public CoordinateDto(int latitude, int longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
