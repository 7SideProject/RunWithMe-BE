package com.runwithme.runwithme.domain.challenge.controller;

import com.runwithme.runwithme.domain.challenge.dto.ChallengeBoardPostDto;
import com.runwithme.runwithme.domain.challenge.dto.ChallengeCreateDto;
import com.runwithme.runwithme.domain.challenge.dto.ChallengeImageDto;
import com.runwithme.runwithme.domain.challenge.entity.Challenge;
import com.runwithme.runwithme.domain.challenge.service.ChallengeService;
import com.runwithme.runwithme.global.dto.PagingResultDto;
import com.runwithme.runwithme.global.result.ResultResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.runwithme.runwithme.global.result.ResultCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/challenge")
@Slf4j
public class ChallengeController {

    private final ChallengeService challengeService;


    @Operation(operationId = "createBoard", summary = "게시글 등록", description = "게시글을 등록한다")
    @PostMapping("/{challengeSeq}/board")
    public ResponseEntity<ResultResponseDto> createBoard(@PathVariable(value = "challengeSeq") Long challengeSeq, @RequestBody ChallengeBoardPostDto challengeBoardPostDto){
        challengeService.createBoard(challengeSeq, challengeBoardPostDto);

        return ResponseEntity.ok().body(ResultResponseDto.of(CREATE_BOARD_SUCCESS));
    }


    @Operation(operationId = "getBoardList", summary = "게시판 조회")
    @GetMapping("/{challengeSeq}/board")
    @PageableAsQueryParam
    public ResponseEntity<ResultResponseDto> getBoardList(@PathVariable(value = "challengeSeq") Long challengeSeq, @Parameter(hidden = true)@PageableDefault Pageable pageable){
        final PagingResultDto pagingResultDto = challengeService.getBoardList(challengeSeq, pageable);

        return ResponseEntity.ok().body(ResultResponseDto.of(GET_ALL_BOARD_SUCCESS, pagingResultDto));
    }


    @Operation(operationId = "deleteBoard", summary = "게시글 삭제")
    @DeleteMapping("/{challengeSeq}/board/{boardSeq}")
    public ResponseEntity<ResultResponseDto> deleteBoard(@PathVariable(value = "boardSeq") Long boardSeq){
        challengeService.deleteBoard(boardSeq);

        return ResponseEntity.ok().body(ResultResponseDto.of(DELETE_BOARD_SUCCESS));
    }

    @Operation(operationId = "createChallenge", summary = "챌린지 등록")
    @PostMapping
    public ResponseEntity<ResultResponseDto> createChallenge(
            @RequestBody ChallengeCreateDto challengeCreateDto,
            @Parameter(name = "file", description = "업로드 사진 데이터")
            @RequestParam(name = "file") ChallengeImageDto imgFile){
        try {
            challengeService.createChallenge(challengeCreateDto, imgFile);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IOException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(operationId = "getChallengeData", summary = "챌린지 상세 조회")
    @GetMapping("/{challengeSeq}")
    public ResponseEntity<ResultResponseDto> getChallengeData(@PathVariable(value = "challengeSeq") Long challengeSeq){
        final Challenge challenge = challengeService.getChallengeData(challengeSeq);

        return ResponseEntity.ok().body(ResultResponseDto.of(GET_ONE_CHALLENGE_SUCCESS, challenge));
    }

    @Operation(operationId = "joinChallengeUser", summary = "챌린지 가입")
    @PostMapping("/{challengeSeq}/join")
    public ResponseEntity<ResultResponseDto> joinChallengeUser(@PathVariable(value = "challengeSeq") Long challengeSeq, String password){
        final boolean success = challengeService.joinChallengeUser(challengeSeq, password);

        if (success) {
            return ResponseEntity.ok().body(ResultResponseDto.of(JOIN_CHALLENGE_SUCCESS));
        } else {
            return ResponseEntity.ok().body(ResultResponseDto.of(JOIN_CHALLENGE_FAIL));
        }
    }

    @Operation(operationId = "joinChallengeUser", summary = "챌린지 가입 여부 체크")
    @GetMapping("/{challengeSeq}/is")
    public ResponseEntity<ResultResponseDto> isChallengeUser(@PathVariable(value = "challengeSeq") Long challengeSeq){
        final boolean success = challengeService.isChallengeUser(challengeSeq);

        if (success) {
            return ResponseEntity.ok().body(ResultResponseDto.of(CHECK_IN_CHALLENGE_SUCCESS));
        } else {
            return ResponseEntity.ok().body(ResultResponseDto.of(CHECK_IN_CHALLENGE_FAIL));
        }
    }

    @Operation(operationId = "getAllChallengeList", summary = "전체 챌린지 리스트 조회")
    @GetMapping("/all")
    @PageableAsQueryParam
    public ResponseEntity<ResultResponseDto> getAllChallengeList(@Parameter(hidden = true)@PageableDefault Pageable pageable){
        final PagingResultDto pagingResultDto = challengeService.getAllChallengeList(pageable);

        return ResponseEntity.ok().body(ResultResponseDto.of(GET_ALL_CHALLENGE_SUCCESS, pagingResultDto));
    }

    @Operation(operationId = "getMyChallengeList", summary = "내 챌린지 리스트 조회")
    @GetMapping("/my")
    @PageableAsQueryParam
    public ResponseEntity<ResultResponseDto> getMyChallengeList(@Parameter(hidden = true)@PageableDefault Pageable pageable){
        final PagingResultDto pagingResultDto = challengeService.getMyChallengeList(pageable);

        return ResponseEntity.ok().body(ResultResponseDto.of(GET_MY_CHALLENGE_SUCCESS, pagingResultDto));
    }
}
