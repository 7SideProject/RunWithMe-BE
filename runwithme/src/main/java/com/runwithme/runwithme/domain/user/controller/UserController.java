package com.runwithme.runwithme.domain.user.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.runwithme.runwithme.domain.user.dto.DuplicatedViewDto;
import com.runwithme.runwithme.domain.user.dto.UserChangePasswordDto;
import com.runwithme.runwithme.domain.user.dto.UserConnectViewDto;
import com.runwithme.runwithme.domain.user.dto.UserCreateDto;
import com.runwithme.runwithme.domain.user.dto.UserLoginDto;
import com.runwithme.runwithme.domain.user.dto.UserProfileDto;
import com.runwithme.runwithme.domain.user.dto.UserProfileImageDto;
import com.runwithme.runwithme.domain.user.dto.UserProfileViewDto;
import com.runwithme.runwithme.domain.user.dto.UserTotalRecordViewDto;
import com.runwithme.runwithme.domain.user.facade.UserFacade;
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

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
    private final UserFacade userFacade;

    @PostMapping(value = "/join", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "회원가입", description = "이메일 회원가입 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {@Content(schema = @Schema(implementation = UserProfileViewDto.class))}),
            @ApiResponse(responseCode = "400", description = "잘못된 파라미터입니다.")
    })
    public ResponseEntity<ResultResponseDto> join(@Validated @RequestBody UserCreateDto dto) {
        UserProfileViewDto response = userService.join(dto);
        log.info("Join user {}", response.seq());
        return new ResponseEntity<>(ResultResponseDto.of(ResultCode.USER_REQUEST_SUCCESS, response), HttpStatus.CREATED);
    }

    @PostMapping(value = "/login")
    @ResponseStatus(HttpStatus.OK)
    public void login(@RequestBody UserLoginDto dto) {}

    @PutMapping(value = "/{userSeq}/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "프로필 설정", description = "유저 프로필을 설정하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = UserProfileViewDto.class))}),
            @ApiResponse(responseCode = "400", description = "잘못된 파라미터입니다.")
    })
    public ResponseEntity<ResultResponseDto> setUserProfile(@PathVariable Long userSeq, @RequestBody UserProfileDto dto) {
        UserProfileViewDto userProfileViewDto = userService.setUserProfile(userSeq, dto);
        log.info("Set profile user {}", userSeq);
        return new ResponseEntity<>(ResultResponseDto.of(ResultCode.USER_REQUEST_SUCCESS, userProfileViewDto), HttpStatus.OK);
    }

    @GetMapping(value = "/{userSeq}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "프로필 조회", description = "유저 프로필을 조회하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = UserProfileViewDto.class))}),
            @ApiResponse(responseCode = "400", description = "잘못된 파라미터입니다.")
    })
    public ResponseEntity<ResultResponseDto> getUserProfile(@PathVariable Long userSeq) {
        UserProfileViewDto userProfileViewDto = userService.getUserProfile(userSeq);
        return new ResponseEntity<>(ResultResponseDto.of(ResultCode.USER_REQUEST_SUCCESS, userProfileViewDto), HttpStatus.OK);
    }

    @GetMapping(value = "/duplicate-email")
    public ResponseEntity<ResultResponseDto> isDuplicatedEmail(@RequestParam String email) {
        DuplicatedViewDto duplicatedViewDto = userService.isDuplicatedEmail(email);
        return new ResponseEntity<>(ResultResponseDto.of(ResultCode.USER_REQUEST_SUCCESS, duplicatedViewDto), HttpStatus.OK);
    }

    @GetMapping(value = "/duplicate-nickname")
    public ResponseEntity<ResultResponseDto> isDuplicatedNickname(@RequestParam String nickname) {
        DuplicatedViewDto duplicatedViewDto = userService.isDuplicatedNickname(nickname);
        return new ResponseEntity<>(ResultResponseDto.of(ResultCode.USER_REQUEST_SUCCESS, duplicatedViewDto), HttpStatus.OK);
    }

    @GetMapping(value = "/{userSeq}/profile-image", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<Resource> getImage(@PathVariable Long userSeq) {
        return new ResponseEntity<>(userService.getUserImage(userSeq), HttpStatus.OK);
    }

    @PutMapping(value = "/{userSeq}/profile-image")
    public ResponseEntity<Void> changeImage(@PathVariable Long userSeq, @ModelAttribute UserProfileImageDto dto) {
        userService.changeImage(userSeq, dto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{userSeq}")
    public ResponseEntity<ResultResponseDto> deleteUser(@PathVariable Long userSeq) {
        userService.delete(userSeq);
        return new ResponseEntity<>(ResultResponseDto.of(ResultCode.USER_REQUEST_SUCCESS), HttpStatus.OK);
    }

//    @PutMapping(value = "/{userSeq}/password")
//    public ResponseEntity<ResultResponseDto> changePassword(@PathVariable Long userSeq, @RequestBody UserChangePasswordDto dto) {
//        userService.changePassword(userSeq, dto);
//        return new ResponseEntity<>(ResultResponseDto.of(ResultCode.USER_REQUEST_SUCCESS), HttpStatus.OK);
//    }

    @PutMapping(value = "/{userEmail}/password")
    public ResponseEntity<ResultResponseDto> changePassword(@PathVariable String userEmail, @RequestBody UserChangePasswordDto dto) {
        userService.changePassword(userEmail, dto);
        return new ResponseEntity<>(ResultResponseDto.of(ResultCode.USER_REQUEST_SUCCESS), HttpStatus.OK);
    }

    @PostMapping(value = "/{userSeq}/connect")
    public ResponseEntity<ResultResponseDto> connect(@PathVariable Long userSeq) {
        boolean isSuccess = userService.connect(userSeq);
        return new ResponseEntity<>(ResultResponseDto.of(ResultCode.USER_REQUEST_SUCCESS, new UserConnectViewDto(isSuccess)), HttpStatus.OK);
    }

    @GetMapping(value = "/{userSeq}/total-record")
    public ResponseEntity<ResultResponseDto> getUserTotalRecord(@PathVariable Long userSeq) {
        UserTotalRecordViewDto userTotalRecord = userFacade.getUserTotalRecord(userSeq);
        return new ResponseEntity<>(ResultResponseDto.of(ResultCode.USER_REQUEST_SUCCESS, userTotalRecord), HttpStatus.OK);
    }
}
