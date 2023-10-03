package com.runwithme.runwithme.domain.challenge.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ChallengeBoardPostDto {
    private String challengeBoardContent;
}
