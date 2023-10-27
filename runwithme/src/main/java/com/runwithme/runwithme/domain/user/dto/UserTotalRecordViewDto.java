package com.runwithme.runwithme.domain.user.dto;

import com.runwithme.runwithme.domain.record.dto.ChallengeTotalRecordResponseDto;

public record UserTotalRecordViewDto(
        Long userSeq,
        ChallengeTotalRecordResponseDto userTotalRecord
) {
}
