package com.runwithme.runwithme.domain.challenge.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeBoardPostDto {
    private String challengeBoardContent;
    private Long imgSeq;
}
