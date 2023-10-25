package com.runwithme.runwithme.domain.challenge.controller;

import static com.runwithme.runwithme.global.result.ResultCode.*;

import java.util.List;
import java.util.Optional;

import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.runwithme.runwithme.domain.challenge.dto.ChallengeBoardResponseDto;
import com.runwithme.runwithme.domain.challenge.dto.ChallengeCreateDto;
import com.runwithme.runwithme.domain.challenge.dto.ChallengeResponseDto;
import com.runwithme.runwithme.domain.challenge.service.ChallengeService;
import com.runwithme.runwithme.global.result.ResultResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/challenge")
@Slf4j
public class ChallengeController {

	private final ChallengeService challengeService;

	@Operation(operationId = "createBoard", summary = "게시글 등록", description = "게시글을 등록한다")
	@PostMapping("/{challengeSeq}/board")
	public ResponseEntity<ResultResponseDto> createBoard(@PathVariable(value = "challengeSeq") Long challengeSeq, @RequestPart(value = "challengeBoardContent") String challengeBoardContent, @RequestPart(value = "image", required = false) MultipartFile image) {
		challengeService.createBoard(challengeSeq, challengeBoardContent, image);
		return ResponseEntity.ok().body(ResultResponseDto.of(CREATE_BOARD_SUCCESS));
	}


	@Operation(operationId = "getBoardList", summary = "게시판 조회")
	@GetMapping("/{challengeSeq}/board")
	@PageableAsQueryParam
	public ResponseEntity<ResultResponseDto> getBoardList(@Parameter(description = "cursorSeq", name = "cursorSeq") Long cursorSeq, @PathVariable(value = "challengeSeq") Long challengeSeq, @Parameter(hidden = true) @PageableDefault Pageable pageable) {
		final List<ChallengeBoardResponseDto> pagingResultDto = challengeService.getBoardList(cursorSeq, challengeSeq, pageable);
		return ResponseEntity.ok().body(ResultResponseDto.of(GET_ALL_BOARD_SUCCESS, pagingResultDto));
	}


	@Operation(operationId = "deleteBoard", summary = "게시글 삭제")
	@DeleteMapping("/{challengeSeq}/board/{boardSeq}")
	public ResponseEntity<ResultResponseDto> deleteBoard(@PathVariable(value = "boardSeq") Long boardSeq, @PathVariable Long challengeSeq) {
		challengeService.deleteBoard(challengeSeq, boardSeq);
		return ResponseEntity.ok().body(ResultResponseDto.of(DELETE_BOARD_SUCCESS));
	}

	@Operation(operationId = "createChallenge", summary = "챌린지 등록")
	@PostMapping
	public ResponseEntity<ResultResponseDto> createChallenge(@RequestPart(value = "challengeCreateDto") ChallengeCreateDto challengeCreateDto, @RequestPart(value = "image", required = false) MultipartFile image) {
		challengeService.createChallenge(challengeCreateDto, image);
		return ResponseEntity.ok().body(ResultResponseDto.of(CREATE_CHALLENGE_SUCCESS));
	}

	@Operation(operationId = "getChallengeData", summary = "챌린지 상세 조회")
	@GetMapping("/{challengeSeq}")
	public ResponseEntity<ResultResponseDto> getChallengeData(@PathVariable(value = "challengeSeq") Long challengeSeq) {
		final Optional<ChallengeResponseDto> challenge = challengeService.getChallengeData(challengeSeq);
		return ResponseEntity.ok().body(ResultResponseDto.of(GET_ONE_CHALLENGE_SUCCESS, challenge));
	}

	@Operation(operationId = "joinChallengeUser", summary = "챌린지 가입")
	@PostMapping("/{challengeSeq}/join")
	public ResponseEntity<ResultResponseDto> joinChallengeUser(@PathVariable(value = "challengeSeq") Long challengeSeq, @RequestParam(name = "password") String password) {
		final boolean success = challengeService.joinChallengeUser(challengeSeq, password);
		return ResponseEntity.ok().body(ResultResponseDto.of(success ? JOIN_CHALLENGE_SUCCESS : JOIN_CHALLENGE_FAIL));
	}

	@Operation(operationId = "joinChallengeUser", summary = "챌린지 가입 여부 체크")
	@GetMapping("/{challengeSeq}/is")
	public ResponseEntity<ResultResponseDto> isChallengeUser(@PathVariable(value = "challengeSeq") Long challengeSeq) {
		final boolean success = challengeService.isChallengeUser(challengeSeq);
		return ResponseEntity.ok().body(ResultResponseDto.of(success ? CHECK_IN_CHALLENGE_SUCCESS : CHECK_IN_CHALLENGE_FAIL));
	}

	@Operation(operationId = "getAllChallengeList", summary = "전체 챌린지 리스트 조회")
	@GetMapping("/all")
	@PageableAsQueryParam
	public ResponseEntity<ResultResponseDto> getAllChallengeList(@Parameter(description = "cursorSeq", name = "cursorSeq") Long cursorSeq, @Parameter(hidden = true) @PageableDefault Pageable pageable) {
		final List<ChallengeResponseDto> pagingResultDto = challengeService.getAllChallengeList(cursorSeq, pageable);
		return ResponseEntity.ok().body(ResultResponseDto.of(GET_ALL_CHALLENGE_SUCCESS, pagingResultDto));
	}

	@Operation(operationId = "getRecruitChallengeList", summary = "모집중인 전체 챌린지 리스트 조회")
	@GetMapping("/all/recruit")
	@PageableAsQueryParam
	public ResponseEntity<ResultResponseDto> getRecruitChallengeList(@Parameter(description = "cursorSeq", name = "cursorSeq") Long cursorSeq, @Parameter(hidden = true) @PageableDefault Pageable pageable) {
		final List<ChallengeResponseDto> challenges = challengeService.getRecruitChallengeList(cursorSeq, pageable);
		return ResponseEntity.ok().body(ResultResponseDto.of(GET_ALL_CHALLENGE_SUCCESS, challenges));
	}

	@Operation(operationId = "getMyChallengeList", summary = "내 챌린지 리스트 조회")
	@GetMapping("/my")
	@PageableAsQueryParam
	public ResponseEntity<ResultResponseDto> getMyChallengeList(@Parameter(description = "cursorSeq", name = "cursorSeq") Long cursorSeq, @Parameter(hidden = true) @PageableDefault Pageable pageable) {
		final List<ChallengeResponseDto> pagingResultDto = challengeService.getMyChallengeList(cursorSeq, pageable);
		return ResponseEntity.ok().body(ResultResponseDto.of(GET_MY_CHALLENGE_SUCCESS, pagingResultDto));
	}

	@Operation(operationId = "deleteMyChallenge", summary = "내 챌린지 탈퇴, delete_yn 의 값을 'Y'로 변경")
	@DeleteMapping("/{challengeSeq}")
	public ResponseEntity<ResultResponseDto> deleteMyChallenge(@PathVariable(value = "challengeSeq") Long challengeSeq) {
		challengeService.deleteMyChallenge(challengeSeq);
		return ResponseEntity.ok().body(ResultResponseDto.of(DELETE_MY_CHALLENGE_SUCCESS));
	}

	@Operation(summary = "게시글 신고")
	@PostMapping("/warn/{boardSeq}")
	public ResponseEntity<ResultResponseDto> boardWarn(@PathVariable(value = "boardSeq") Long boardSeq) {
		final boolean success = challengeService.boardWarn(boardSeq);

		if (success) {
			return ResponseEntity.ok().body(ResultResponseDto.of(WARN_BOARD_SUCCESS));
		} else {
			return ResponseEntity.ok().body(ResultResponseDto.of(WARN_BOARD_FAIL));
		}
	}
}
