package com.runwithme.runwithme.domain.challenge.dto;

import java.time.LocalDate;

import com.runwithme.runwithme.domain.challenge.entity.GoalType;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeCreateDto {
    private String name;
    private String description;
    @Min(value = 1)
    @Max(value = 7)
    private Long goalDays;
    private GoalType goalType;
    private Long goalAmount;
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private String password;
    @Min(value = 500)
    @Max(value = 10000)
    private Long cost;
    @Min(value = 2)
    @Max(value = 20)
    private Long maxMember;
}
