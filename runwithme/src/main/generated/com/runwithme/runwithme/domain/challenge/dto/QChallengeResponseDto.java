package com.runwithme.runwithme.domain.challenge.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.runwithme.runwithme.domain.challenge.dto.QChallengeResponseDto is a Querydsl Projection type for ChallengeResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QChallengeResponseDto extends ConstructorExpression<ChallengeResponseDto> {

    private static final long serialVersionUID = -990762904L;

    public QChallengeResponseDto(com.querydsl.core.types.Expression<Long> seq, com.querydsl.core.types.Expression<Long> managerSeq, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<Long> imgSeq, com.querydsl.core.types.Expression<Long> goalDays, com.querydsl.core.types.Expression<String> goalType, com.querydsl.core.types.Expression<Long> goalAmount, com.querydsl.core.types.Expression<java.time.LocalDateTime> timeStart, com.querydsl.core.types.Expression<java.time.LocalDateTime> timeEnd) {
        super(ChallengeResponseDto.class, new Class<?>[]{long.class, long.class, String.class, long.class, long.class, String.class, long.class, java.time.LocalDateTime.class, java.time.LocalDateTime.class}, seq, managerSeq, name, imgSeq, goalDays, goalType, goalAmount, timeStart, timeEnd);
    }

}

