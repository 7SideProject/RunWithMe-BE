package com.runwithme.runwithme.domain.record.dto;

import lombok.Data;

@Data
public class RecordCoordinateDto {
    private Long coordinateSeq;
    private int latitude;
    private int longitude;
}
