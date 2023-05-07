package com.runwithme.runwithme.domain.user.controller;

import com.runwithme.runwithme.domain.user.dto.DuplicatedViewDto;
import com.runwithme.runwithme.domain.user.dto.UserCreateDto;
import com.runwithme.runwithme.domain.user.dto.UserProfileDto;
import com.runwithme.runwithme.domain.user.dto.UserProfileViewDto;
import com.runwithme.runwithme.domain.user.service.UserService;
import com.runwithme.runwithme.global.result.ResultCode;
import com.runwithme.runwithme.global.result.ResultResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/join", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "회원가입", description = "이메일 회원가입 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = { @Content(schema = @Schema(implementation = UserProfileViewDto.class)) }),
            @ApiResponse(responseCode = "400", description = "잘못된 파라미터입니다.")
    })
    public ResponseEntity<ResultResponseDto> join(@RequestBody UserCreateDto dto) {
        try {
            UserProfileViewDto response = userService.join(dto);
            log.info("Join user {}", response.seq());
            return new ResponseEntity<>(ResultResponseDto.of(ResultCode.USER_REQUEST_SUCCESS, response), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(ResultResponseDto.of(ResultCode.INVALID_PARAMETER_FAIL, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/{userSeq}/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "프로필 설정", description = "유저 프로필을 설정하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = UserProfileViewDto.class)) }),
            @ApiResponse(responseCode = "400", description = "잘못된 파라미터입니다.")
    })
    public ResponseEntity<ResultResponseDto> setUserProfile(@PathVariable Long userSeq, @RequestBody UserProfileDto dto) {
        try {
            UserProfileViewDto userProfileViewDto = userService.setUserProfile(userSeq, dto);
            log.info("Set profile user {}", userSeq);
            return new ResponseEntity<>(ResultResponseDto.of(ResultCode.USER_REQUEST_SUCCESS, userProfileViewDto), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(ResultResponseDto.of(ResultCode.INVALID_PARAMETER_FAIL), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{userSeq}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "프로필 조회", description = "유저 프로필을 조회하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = UserProfileViewDto.class)) }),
            @ApiResponse(responseCode = "400", description = "잘못된 파라미터입니다.")
    })
    public ResponseEntity<ResultResponseDto> getUserProfile(@PathVariable Long userSeq) {
        try {
            UserProfileViewDto userProfileViewDto = userService.getUserProfile(userSeq);
            log.info("Get profile user {}", userSeq);
            return new ResponseEntity<>(ResultResponseDto.of(ResultCode.USER_REQUEST_SUCCESS, userProfileViewDto), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(ResultResponseDto.of(ResultCode.INVALID_PARAMETER_FAIL, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/duplicate-email")
    public ResponseEntity<ResultResponseDto> isDuplicatedEmail(@RequestParam String email) {
        DuplicatedViewDto duplicatedViewDto = userService.isDuplicatedEmail(email);
        log.info("Success process");
        return new ResponseEntity<>(ResultResponseDto.of(ResultCode.USER_REQUEST_SUCCESS, duplicatedViewDto), HttpStatus.OK);
    }

    @GetMapping(value = "/duplicate-nickname")
    public ResponseEntity<ResultResponseDto> isDuplicatedNickname(@RequestParam String nickname) {
        DuplicatedViewDto duplicatedViewDto = userService.isDuplicatedNickname(nickname);
        log.info("Success process");
        return new ResponseEntity<>(ResultResponseDto.of(ResultCode.USER_REQUEST_SUCCESS, duplicatedViewDto), HttpStatus.OK);
    }
}
