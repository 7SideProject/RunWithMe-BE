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

    public QChallengeResponseDto(com.querydsl.core.types.Expression<Long> seq, com.querydsl.core.types.Expression<Long> managerSeq, com.querydsl.core.types.Expression<String> managerName, com.querydsl.core.types.Expression<? extends com.runwithme.runwithme.global.entity.Image> image, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<Long> goalDays, com.querydsl.core.types.Expression<String> goalType, com.querydsl.core.types.Expression<Long> goalAmount, com.querydsl.core.types.Expression<java.time.LocalDate> timeStart, com.querydsl.core.types.Expression<java.time.LocalDate> timeEnd, com.querydsl.core.types.Expression<Long> nowMember, com.querydsl.core.types.Expression<Long> maxMember, com.querydsl.core.types.Expression<Long> cost, com.querydsl.core.types.Expression<Boolean> challengeUserFlag) {
        super(ChallengeResponseDto.class, new Class<?>[]{long.class, long.class, String.class, com.runwithme.runwithme.global.entity.Image.class, String.class, long.class, String.class, long.class, java.time.LocalDate.class, java.time.LocalDate.class, long.class, long.class, long.class, boolean.class}, seq, managerSeq, managerName, image, name, goalDays, goalType, goalAmount, timeStart, timeEnd, nowMember, maxMember, cost, challengeUserFlag);
    }

}

