package com.runwithme.runwithme.domain.challenge.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.runwithme.runwithme.domain.challenge.dto.QChallengeBoardResponseDto is a Querydsl Projection type for ChallengeBoardResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QChallengeBoardResponseDto extends ConstructorExpression<ChallengeBoardResponseDto> {

    private static final long serialVersionUID = -744982834L;

    public QChallengeBoardResponseDto(com.querydsl.core.types.Expression<Long> boardSeq, com.querydsl.core.types.Expression<Long> userSeq, com.querydsl.core.types.Expression<String> nickname, com.querydsl.core.types.Expression<Long> imgSeq, com.querydsl.core.types.Expression<String> content) {
        super(ChallengeBoardResponseDto.class, new Class<?>[]{long.class, long.class, String.class, long.class, String.class}, boardSeq, userSeq, nickname, imgSeq, content);
    }

}

