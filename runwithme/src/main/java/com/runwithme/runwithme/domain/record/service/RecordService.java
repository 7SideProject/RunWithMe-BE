package com.runwithme.runwithme.domain.record.service;

import static com.runwithme.runwithme.global.result.ResultCode.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.runwithme.runwithme.domain.challenge.repository.ChallengeUserRepository;
import com.runwithme.runwithme.domain.record.dto.ChallengeTotalRecordResponseDto;
import com.runwithme.runwithme.domain.challenge.entity.Challenge;
import com.runwithme.runwithme.domain.challenge.repository.ChallengeRepository;
import com.runwithme.runwithme.domain.record.dto.CoordinateDto;
import com.runwithme.runwithme.domain.record.dto.RecordWeeklyCountDto;
import com.runwithme.runwithme.domain.record.dto.RunRecordPostDto;
import com.runwithme.runwithme.domain.record.dto.RunRecordResponseDto;
import com.runwithme.runwithme.domain.record.entity.ChallengeTotalRecord;
import com.runwithme.runwithme.domain.record.entity.RecordCoordinate;
import com.runwithme.runwithme.domain.record.entity.RunRecord;
import com.runwithme.runwithme.domain.record.repository.ChallengeTotalRecordRepository;
import com.runwithme.runwithme.domain.record.repository.RecordCoordinateRepository;
import com.runwithme.runwithme.domain.record.repository.RunRecordRepository;
import com.runwithme.runwithme.domain.user.entity.User;
import com.runwithme.runwithme.global.entity.Image;
import com.runwithme.runwithme.global.error.CustomException;
import com.runwithme.runwithme.global.service.ImageService;
import com.runwithme.runwithme.global.utils.AuthUtils;
import com.runwithme.runwithme.global.utils.ImageCache;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecordService {
	private final ChallengeRepository challengeRepository;
	private final ChallengeUserRepository challengeUserRepository;
	private final RunRecordRepository runRecordRepository;
	private final RecordCoordinateRepository recordCoordinateRepository;
	private final ChallengeTotalRecordRepository challengeTotalRecordRepository;
	private final AuthUtils authUtils;
	private final ImageService imageService;

	@Transactional
	public void createRunRecord(Long challengeSeq, RunRecordPostDto runRecordPostDto, MultipartFile image) {
		final User user = authUtils.getLoginUser();

		if (!challengeUserRepository.existsByUserSeqAndChallengeSeq(user.getSeq(), challengeSeq)) {
			throw new CustomException(CHALLENGE_NOT_JOIN);
		}

		final Challenge challenge = challengeRepository.findById(challengeSeq)
			.orElseThrow(() -> new CustomException(CHALLENGE_NOT_FOUND));

		final LocalDate nowTime = LocalDate.now();

		if (challenge.getDateStart().isAfter(nowTime)) {
			throw new CustomException(CHALLENGE_NOT_START);
		}

		if (challenge.getDateEnd().isBefore(nowTime)) {
			throw new CustomException(CHALLENGE_AFTER_END);
		}

		if (runRecordRepository.existsByUserSeqAndChallengeSeqAndRegTime(user.getSeq(), challengeSeq, LocalDate.now())) {
			throw new CustomException(RECORD_ALREADY_EXIST);
		}

		final Image savedImage = imageIsEmpty(image);

		final RunRecord runRecord = RunRecord.builder()
			.user(user)
			.challengeSeq(challengeSeq)
			.runningDay(runRecordPostDto.getRunningDay())
			.startTime(runRecordPostDto.getStartTime())
			.endTime(runRecordPostDto.getEndTime())
			.runningTime(runRecordPostDto.getRunningTime())
			.runningDistance(runRecordPostDto.getRunningDistance())
			.calorie(runRecordPostDto.getCalorie())
			.avgSpeed(runRecordPostDto.getAvgSpeed())
			.image(savedImage)
			.successYn(runRecordPostDto.getSuccessYn())
			.weekly(getWeekly(challengeSeq))
			.build();
		runRecordRepository.save(runRecord);

		final List<CoordinateDto> coordinates = runRecordPostDto.getCoordinates();
		for (CoordinateDto coordinate : coordinates) {
			final RecordCoordinate recordCoordinate = RecordCoordinate.builder()
				.runRecord(runRecord)
				.latitude(coordinate.getLatitude())
				.longitude(coordinate.getLongitude())
				.build();
			recordCoordinateRepository.save(recordCoordinate);
		}

		final ChallengeTotalRecord myTotals = challengeTotalRecordRepository.findByUserSeqAndChallengeSeq(user.getSeq(), challengeSeq);

		myTotals.plusTotalTime(runRecordPostDto.getRunningTime());
		myTotals.plusTotalDistance(runRecordPostDto.getRunningDistance());
		myTotals.plusTotalCalorie(runRecordPostDto.getCalorie());

		myTotals.setTotalAvgSpeed((myTotals.getTotalDistance().doubleValue() / 1000) / (myTotals.getTotalTime().doubleValue() / 3600));

		if (myTotals.getTotalLongestTime() < runRecordPostDto.getRunningTime()) {
			myTotals.setTotalLongestTime(runRecordPostDto.getRunningTime());
		}

		if (myTotals.getTotalLongestDistance() < runRecordPostDto.getRunningDistance()) {
			myTotals.setTotalLongestDistance(runRecordPostDto.getRunningDistance());
		}
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
	public List<RunRecordResponseDto> getAllRunRecord(Long challengeSeq, Long cursorSeq, Pageable pageable) {
		return runRecordRepository.findAllRecordPage(cursorSeq, challengeSeq, pageable).getContent();
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

	public Image imageIsEmpty(MultipartFile image) {
		if (image.isEmpty()) {
			return ImageCache.get(ImageCache.DEFAULT_CHALLENGE);
		}
		return imageService.save(image);
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

	public ChallengeTotalRecordResponseDto getUserTotalRecord(Long userSeq) {
		List<ChallengeTotalRecord> userTotalRecord = challengeTotalRecordRepository.findByUserSeq(userSeq);

		long totalTime = 0;
		long totalDistance = 0;
		long totalCalorie = 0;
		long totalLongestTime = 0;
		long totalLongestDistance = 0;
		long totalSpeed = 0;
		for (ChallengeTotalRecord record : userTotalRecord) {
			totalTime += record.getTotalTime();
			totalDistance += record.getTotalDistance();
			totalCalorie += record.getTotalCalorie();
			totalLongestTime = Math.max(totalLongestTime, record.getTotalLongestTime());
			totalLongestDistance = Math.max(totalLongestDistance, record.getTotalLongestDistance());
			totalSpeed += record.getTotalAvgSpeed();
		}
		long totalAvgSpeed = userTotalRecord.isEmpty() ? 0 : totalSpeed / userTotalRecord.size();

		return new ChallengeTotalRecordResponseDto(totalTime, totalDistance, totalCalorie, totalLongestTime, totalLongestDistance, totalAvgSpeed);
	}
}
