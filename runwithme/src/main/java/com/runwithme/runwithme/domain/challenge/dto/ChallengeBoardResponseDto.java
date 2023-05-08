package com.runwithme.runwithme.domain.challenge.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeBoardResponseDto {
    private Long boardSeq;
    private Long userSeq;
    private String nickname;
    private Long imgSeq;
    private String content;
}
