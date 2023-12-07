package com.runwithme.runwithme.global.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.runwithme.runwithme.domain.challenge.dto.ChallengeEndDto;
import com.runwithme.runwithme.domain.challenge.service.ChallengeService;
import com.runwithme.runwithme.domain.record.dto.RecordWeeklyCountDto;
import com.runwithme.runwithme.domain.record.service.RecordService;
import com.runwithme.runwithme.domain.user.entity.PointType;
import com.runwithme.runwithme.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ScheduleTasks {
	private final UserService userService;
	private final RecordService recordService;
	private final ChallengeService challengeService;
	/*
		@AUTHOR: 태영
		TODO: log common format, 추후 properties에 빼서 공통으로 사용할 것
	 */
	private final SimpleDateFormat LOG_TIMESTAMP_FMT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSSS\n");

	@Scheduled(cron = "* * 5 * * *", zone = "Asia/Seoul")
	public void pointDistribution() {
		Date now = new Date();
		String ima = LOG_TIMESTAMP_FMT.format(now);
		System.out.printf("현재 시간: %s, Scheduler 작동 시작\n", ima);

		/*
		 특정 시간에 끝난 챌린지에 등록된 유저들에게 포인트 재분배
		 TODO: 0. RunRecord Insert 시 weekly 계산하여 삽입
		 TODO: 1. 끝나는 날짜로 등록된 챌린지 전부 가져오기		-> ChallengeEndDtoList
		 TODO: 2. successYN으로 실패 유저 분리					-> failedUserSet
		 TODO: 3. failedUserSet으로 성공 유저 분리				-> successUserSet
		 TODO: 4. 한번도 안뛴 사람 수 계산						-> neverRunCount
		 TODO: 5. 실패 한 유저 포인트 총합 계산					-> failedSumPoint
		 TODO: 6. 실패한 유저 포인트 총합 / 성공한 유저 수		-> earnPoint
		 TODO: 7. 성공한 유저 포인트 증가
		 */
		Set<Long> failedUserSeqSet = new HashSet<>();
		Set<Long> successUserSeqSet = new HashSet<>();

		// 1.
		for (ChallengeEndDto challengeEndDto : challengeService.findByDateEndIsToday()) {
			List<RecordWeeklyCountDto> recordWeeklyCountDtoList = recordService.getWeeklySuccessYCount(challengeEndDto.getSeq());

			// 2.
			for (RecordWeeklyCountDto recordWeeklyCountDto : recordWeeklyCountDtoList) {
				if (recordWeeklyCountDto.getSuccessCount() < challengeEndDto.getGoalDays()) {
					failedUserSeqSet.add(recordWeeklyCountDto.getUserSeq());
				}
			}

			// 3.
			for (RecordWeeklyCountDto recordWeeklyCountDto : recordWeeklyCountDtoList) {
				if (!failedUserSeqSet.contains(recordWeeklyCountDto.getUserSeq()))
					successUserSeqSet.add(recordWeeklyCountDto.getUserSeq());
			}

			// 4.
			int neverRunCount = challengeService.getChallengeCount(challengeEndDto.getSeq()) - recordWeeklyCountDtoList.size();

			// 5.
			long failedSumPoint = challengeEndDto.getCost() * (failedUserSeqSet.size() + neverRunCount);

			// 6.
			int earnPoint = (int) ((failedSumPoint / successUserSeqSet.size()) * 10 / 10);

			// 7.
			for (Long successUserSeq : successUserSeqSet) {
				userService.updateUserPoint(successUserSeq, earnPoint, PointType.PLUS);
			}
		}

		System.out.printf("현재 시간: %s, Scheduler 작동 완료\n", ima);
	}
}
