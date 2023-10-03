package com.runwithme.runwithme.global.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class PagingResultDto<T> {
    private List<T> result;
}
