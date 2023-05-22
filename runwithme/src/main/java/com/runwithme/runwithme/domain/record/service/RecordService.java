package com.runwithme.runwithme.domain.record.service;

import com.runwithme.runwithme.domain.record.dto.CoordinateDto;
import com.runwithme.runwithme.domain.record.dto.RunRecordPostDto;
import com.runwithme.runwithme.domain.record.entity.ChallengeTotalRecord;
import com.runwithme.runwithme.domain.record.entity.RunRecord;
import com.runwithme.runwithme.domain.record.repository.ChallengeTotalRecordRepository;
import com.runwithme.runwithme.domain.record.repository.RunRecordRepository;
import com.runwithme.runwithme.global.error.exception.EntityAlreadyExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.runwithme.runwithme.global.error.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecordService {

    private final RunRecordRepository runRecordRepository;
    private final ChallengeTotalRecordRepository challengeTotalRecordRepository;

    @Transactional
    public void createRunRecord(Long challengeSeq, RunRecordPostDto runRecordPostDto){
        final Long userSeq = new Long(1);

        if (runRecordRepository.existsByUserSeqAndChallengeSeqAndRegTime(userSeq, challengeSeq, LocalDate.now())){
            throw new EntityAlreadyExistException(RECORD_ALREADY_EXIST);
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
    public ChallengeTotalRecord getTotalRecord(Long challengeSeq){
        final Long userSeq = new Long(1);

        final ChallengeTotalRecord myTotals = challengeTotalRecordRepository.findByUserSeqAndChallengeSeq(userSeq, challengeSeq);

        return myTotals;
    }

    @Transactional
    public List<RunRecord> getMyRunRecord(Long challengeSeq){
        final Long userSeq = new Long(1);

        final List<RunRecord> myRunRecords = runRecordRepository.findAllByUserSeqAndChallengeSeq(userSeq, challengeSeq);

        return myRunRecords;
    }

    @Transactional
    public List<RunRecord> getAllRunRecord(Long challengeSeq){
        final Long userSeq = new Long(1);

        final List<RunRecord> allRunRecords = runRecordRepository.findAllByChallengeSeq(challengeSeq);

        return allRunRecords;
    }

    @Transactional
    public RunRecord getRunRecord(Long runRecordSeq){
        final Long userSeq = new Long(1);

        final RunRecord runRecord = runRecordRepository.findById(runRecordSeq).get();

        return runRecord;
    }

    @Transactional
    public boolean addCoordinate(Long recordSeq, List<CoordinateDto> coordinates){
        int[] results = runRecordRepository.coordinatesInsertBatch(recordSeq, coordinates);

        int success = 0;
        int fail = 0;

        for (int i = 0; i < results.length; i++) {
            if (results[i] == -2) {
                success++;
            } else {
                fail++;
            }
        }

        if (results.length == success) {
            return true;
        } else {
            return false;
        }
    }
}
