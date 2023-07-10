package com.runwithme.runwithme.domain.record.service;

import com.runwithme.runwithme.domain.record.dto.CoordinateDto;
import com.runwithme.runwithme.domain.record.dto.RunRecordPostDto;
import com.runwithme.runwithme.domain.record.entity.ChallengeTotalRecord;
import com.runwithme.runwithme.domain.record.entity.RunRecord;
import com.runwithme.runwithme.domain.record.repository.ChallengeTotalRecordRepository;
import com.runwithme.runwithme.domain.record.repository.RunRecordRepository;
import com.runwithme.runwithme.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.runwithme.runwithme.global.result.ResultCode.RECORD_ALREADY_EXIST;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecordService {

    private final RunRecordRepository runRecordRepository;
    private final ChallengeTotalRecordRepository challengeTotalRecordRepository;

    @Transactional
    public void createRunRecord(Long challengeSeq, RunRecordPostDto runRecordPostDto) {
        final Long userSeq = 1L;

        if (runRecordRepository.existsByUserSeqAndChallengeSeqAndRegTime(userSeq, challengeSeq, LocalDate.now())) {
            throw new CustomException(RECORD_ALREADY_EXIST);
        }

        final RunRecord runRecord = RunRecord.builder()
                .userSeq(userSeq)
                .challengeSeq(challengeSeq)
                .startTime(runRecordPostDto.getStartTime())
                .endTime(runRecordPostDto.getEndTime())
                .runningTime(runRecordPostDto.getRunningTime())
                .runningDistance(runRecordPostDto.getRunningDistance())
//                .coordinates(runRecordPostDto.getCoordinates())
                .imgSeq(runRecordPostDto.getImgSeq()).build();
        runRecordRepository.save(runRecord);

        final ChallengeTotalRecord myTotals = challengeTotalRecordRepository.findByUserSeqAndChallengeSeq(userSeq, challengeSeq);

        myTotals.setTotalTime(myTotals.getTotalTime() + runRecordPostDto.getRunningTime());
        myTotals.setTotalDistance(myTotals.getTotalDistance() + runRecordPostDto.getRunningDistance());
    }

    @Transactional
    public ChallengeTotalRecord getTotalRecord(Long challengeSeq) {
        final Long userSeq = 1L;
        return challengeTotalRecordRepository.findByUserSeqAndChallengeSeq(userSeq, challengeSeq);
    }

    @Transactional
    public List<RunRecord> getMyRunRecord(Long challengeSeq) {
        final Long userSeq = 1L;
        return runRecordRepository.findAllByUserSeqAndChallengeSeq(userSeq, challengeSeq);
    }

    @Transactional
    public List<RunRecord> getAllRunRecord(Long challengeSeq) {
        return runRecordRepository.findAllByChallengeSeq(challengeSeq);
    }

    @Transactional
    public RunRecord getRunRecord(Long runRecordSeq) {
        return runRecordRepository.findById(runRecordSeq).get();
    }

    @Transactional
    public boolean addCoordinate(Long recordSeq, List<CoordinateDto> coordinates) {
        int[] results = runRecordRepository.coordinatesInsertBatch(recordSeq, coordinates);
        int success = 0;

        for (int result : results) {
            if (result == -2) {
                success++;
            }
        }
        return results.length == success;
    }
}
