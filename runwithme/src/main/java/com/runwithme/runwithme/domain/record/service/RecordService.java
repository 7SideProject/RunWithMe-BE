package com.runwithme.runwithme.domain.record.service;

import static com.runwithme.runwithme.global.result.ResultCode.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.runwithme.runwithme.domain.challenge.dto.ChallengeImageDto;
import com.runwithme.runwithme.domain.challenge.entity.Challenge;
import com.runwithme.runwithme.domain.challenge.repository.ChallengeRepository;
import com.runwithme.runwithme.domain.record.dto.CoordinateDto;
import com.runwithme.runwithme.domain.record.dto.RecordWeeklyCountDto;
import com.runwithme.runwithme.domain.record.dto.RunRecordPostDto;
import com.runwithme.runwithme.domain.record.entity.ChallengeTotalRecord;
import com.runwithme.runwithme.domain.record.entity.RunRecord;
import com.runwithme.runwithme.domain.record.repository.ChallengeTotalRecordRepository;
import com.runwithme.runwithme.domain.record.repository.RunRecordRepository;
import com.runwithme.runwithme.global.entity.Image;
import com.runwithme.runwithme.global.error.CustomException;
import com.runwithme.runwithme.global.service.ImageService;
import com.runwithme.runwithme.global.utils.AuthUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecordService {
	private final RunRecordRepository runRecordRepository;
	private final ChallengeRepository challengeRepository;
	private final ChallengeTotalRecordRepository challengeTotalRecordRepository;
	private final AuthUtils authUtils;
	private final ImageService imageService;

	@Transactional
	public void createRunRecord(Long challengeSeq, RunRecordPostDto runRecordPostDto, ChallengeImageDto imgFile) throws IOException {
		final Long userSeq = authUtils.getLoginUserSeq();

		final Image savedImage = imageService.save(imgFile.image());

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
			.image(savedImage)
			.successYn(runRecordPostDto.getSuccessYn())
			.weekly(getWeekly(challengeSeq))
			.build();
		runRecordRepository.save(runRecord);

		final ChallengeTotalRecord myTotals = challengeTotalRecordRepository.findByUserSeqAndChallengeSeq(userSeq, challengeSeq);

		myTotals.setTotalTime(myTotals.getTotalTime() + runRecordPostDto.getRunningTime());
		myTotals.setTotalDistance(myTotals.getTotalDistance() + runRecordPostDto.getRunningDistance());
	}

	@Transactional
	public ChallengeTotalRecord getTotalRecord(Long challengeSeq) {
		final Long userSeq = authUtils.getLoginUserSeq();
		return challengeTotalRecordRepository.findByUserSeqAndChallengeSeq(userSeq, challengeSeq);
	}

	@Transactional
	public List<RunRecord> getMyRunRecord(Long challengeSeq) {
		final Long userSeq = authUtils.getLoginUserSeq();
		return runRecordRepository.findAllByUserSeqAndChallengeSeq(userSeq, challengeSeq);
	}

	@Transactional
	public List<RunRecord> getAllRunRecord(Long challengeSeq) {
		return runRecordRepository.findAllByChallengeSeq(challengeSeq);
	}

	@Transactional
	public RunRecord getRunRecord(Long runRecordSeq) {
		return runRecordRepository.findById(runRecordSeq)
			.orElseThrow(() -> new CustomException(USER_NOT_FOUND));
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

	public List<RecordWeeklyCountDto> getWeeklySuccessYCount(Long challengeSeq) {
		return runRecordRepository.getWeeklySuccessYCount(challengeSeq);
	}

	public int getWeekly(Long challengeSeq) {
		Challenge challenge = challengeRepository.findById(challengeSeq).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
		LocalDate challengeStartDate = challenge.getDateStart();
		int diff = (int) ChronoUnit.DAYS.between(LocalDate.now(), challengeStartDate);
		return (diff - 1) / 7 + 1;
	}
}
