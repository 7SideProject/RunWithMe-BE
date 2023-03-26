package com.runwithme.runwithme.domain.user.controller;

import com.runwithme.runwithme.domain.user.dto.UserProfileDto;
import com.runwithme.runwithme.domain.user.dto.UserProfileViewDto;
import com.runwithme.runwithme.domain.user.service.UserService;
import com.runwithme.runwithme.global.result.ResultCode;
import com.runwithme.runwithme.global.result.ResultResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/{userSeq}/profile")
    public ResponseEntity<ResultResponseDto> setUserProfile(@PathVariable Long userSeq, @RequestBody UserProfileDto dto) {
        try {
            UserProfileViewDto userProfileViewDto = userService.setUserProfile(userSeq, dto);
            log.info("Set profile user {}", userSeq);
            return new ResponseEntity<>(ResultResponseDto.of(ResultCode.USER_REQUEST_SUCCESS, userProfileViewDto), HttpStatus.OK);
        } catch (IllegalStateException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(ResultResponseDto.of(ResultCode.INVALID_PARAMETER_FAIL), HttpStatus.BAD_REQUEST);
        }
    }
}
