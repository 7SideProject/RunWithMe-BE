package com.runwithme.runwithme.domain.record.controller;

import static com.runwithme.runwithme.global.result.ResultCode.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.runwithme.runwithme.domain.record.dto.CoordinateDto;
import com.runwithme.runwithme.domain.record.dto.RunRecordDetailDto;
import com.runwithme.runwithme.domain.record.dto.RunRecordPostDto;
import com.runwithme.runwithme.domain.record.dto.RunRecordResponseDto;
import com.runwithme.runwithme.domain.record.entity.ChallengeTotalRecord;
import com.runwithme.runwithme.domain.record.entity.RunRecord;
import com.runwithme.runwithme.domain.record.service.RecordService;
import com.runwithme.runwithme.global.result.ResultResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/challenge")
@Slf4j
public class RecordController {
    private final RecordService recordService;

    @Operation(operationId = "createRunRecord", summary = "기록 등록", description = "기록을 등록한다")
    @PostMapping("/{challengeSeq}/record")
    public ResponseEntity<ResultResponseDto> createRunRecord(@PathVariable(value = "challengeSeq") Long challengeSeq, @RequestPart(value = "runRecordPostDto") RunRecordPostDto runRecordPostDto, @RequestPart(value = "image", required = false) MultipartFile image){
        recordService.createRunRecord(challengeSeq, runRecordPostDto, image);
        return ResponseEntity.ok().body(ResultResponseDto.of(CREATE_RECORD_SUCCESS));
    }

    @Operation(operationId = "getTotalRecord", summary = "챌린지내 내 누적기록 수치 조회")
    @GetMapping("/{challengeSeq}/record/my-total")
    public ResponseEntity<ResultResponseDto> getTotalRecord(@PathVariable(value = "challengeSeq") Long challengeSeq) {
        final ChallengeTotalRecord myTotals = recordService.getTotalRecord(challengeSeq);
        return ResponseEntity.ok().body(ResultResponseDto.of(GET_MY_TOTAL_RECORD_SUCCESS, myTotals));
    }

    @Operation(operationId = "getMyRunRecord", summary = "챌린지내 내 기록 조회")
    @GetMapping("/{challengeSeq}/record/my")
    public ResponseEntity<ResultResponseDto> getMyRunRecord(@PathVariable(value = "challengeSeq") Long challengeSeq) {
        final List<RunRecord> myRunRecords = recordService.getMyRunRecord(challengeSeq);
        return ResponseEntity.ok().body(ResultResponseDto.of(GET_MY_RECORD_SUCCESS, myRunRecords));
    }

    @Operation(operationId = "getAllRunRecord", summary = "챌린지원 기록 리스트 조회")
    @GetMapping("/{challengeSeq}/record/all")
    public ResponseEntity<ResultResponseDto> getAllRunRecord(@PathVariable(value = "challengeSeq") Long challengeSeq, @Parameter(description = "cursorSeq", name = "cursorSeq") Long cursorSeq, @Parameter(hidden = true) @PageableDefault Pageable pageable) {
        final List<RunRecordResponseDto> pagingResultDto = recordService.getAllRunRecord(challengeSeq, cursorSeq, pageable);
        return ResponseEntity.ok().body(ResultResponseDto.of(GET_ALL_RECORD_SUCCESS, pagingResultDto));
    }

    // 기록 seq로 상세조회
    @Operation(operationId = "getRecord", summary = "기록 상세조회")
    @GetMapping("/record/{runRecordSeq}")
    public ResponseEntity<ResultResponseDto> getRunRecord(@PathVariable(value = "runRecordSeq") Long runRecordSeq) {
        final RunRecordDetailDto runRecord = recordService.getRunRecord(runRecordSeq);
        return ResponseEntity.ok().body(ResultResponseDto.of(GET_ONE_RECORD_SUCCESS, runRecord));
    }

    @Operation(operationId = "addCoordinate", summary = "Record Coordinate 등록")
    @PostMapping("/{recordSeq}/coordinate")
    public ResponseEntity<ResultResponseDto> addCoordinate(@PathVariable(value = "recordSeq") Long recordSeq, @RequestBody List<CoordinateDto> coordinates) {
        final boolean success = recordService.addCoordinate(recordSeq, coordinates);
        return ResponseEntity.ok().body(ResultResponseDto.of(success ? ADD_COORDINATES_FAIL : ADD_COORDINATES_SUCCESS));
    }
}
