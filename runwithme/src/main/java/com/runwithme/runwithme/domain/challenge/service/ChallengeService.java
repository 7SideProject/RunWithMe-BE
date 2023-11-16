package com.runwithme.runwithme.domain.challenge.service;

import static com.runwithme.runwithme.global.result.ResultCode.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.runwithme.runwithme.domain.challenge.dto.*;
import com.runwithme.runwithme.domain.record.entity.ChallengeTotalRecord;
import com.runwithme.runwithme.domain.record.repository.ChallengeTotalRecordRepository;
import com.runwithme.runwithme.global.utils.ImageCache;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.runwithme.runwithme.domain.challenge.entity.Challenge;
import com.runwithme.runwithme.domain.challenge.entity.ChallengeBoard;
import com.runwithme.runwithme.domain.challenge.entity.ChallengeBoardWarn;
import com.runwithme.runwithme.domain.challenge.entity.ChallengeUser;
import com.runwithme.runwithme.domain.challenge.repository.ChallengeBoardRepository;
import com.runwithme.runwithme.domain.challenge.repository.ChallengeBoardWarnRepository;
import com.runwithme.runwithme.domain.challenge.repository.ChallengeRepository;
import com.runwithme.runwithme.domain.challenge.repository.ChallengeUserRepository;
import com.runwithme.runwithme.domain.user.entity.User;
import com.runwithme.runwithme.global.entity.Image;
import com.runwithme.runwithme.global.error.CustomException;
import com.runwithme.runwithme.global.service.ImageService;
import com.runwithme.runwithme.global.utils.AuthUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChallengeService {
	private final ChallengeRepository challengeRepository;
	private final ChallengeBoardRepository challengeBoardRepository;
	private final ChallengeUserRepository challengeUserRepository;
	private final ChallengeBoardWarnRepository challengeBoardWarnRepository;
	private final ChallengeTotalRecordRepository challengeTotalRecordRepository;
	private final ImageService imageService;
	private final AuthUtils authUtils;

	@Transactional
	public void createBoard(Long challengeSeq, String challengeBoardContent, MultipartFile image) {
		final User user = authUtils.getLoginUser();

		final Image savedImage = imageIsEmpty(image);

		final LocalDateTime challengeBoardRegTime = LocalDateTime.now();
		final ChallengeBoard challengeBoard = ChallengeBoard.builder()
			.user(user)
			.challengeSeq(challengeSeq)
			.image(savedImage)
			.challengeBoardContent(challengeBoardContent)
			.challengeBoardRegTime(challengeBoardRegTime).build();
		challengeBoardRepository.save(challengeBoard);
	}

	@Transactional
	public List<ChallengeBoardResponseDto> getBoardList(Long cursorSeq, Long challengeSeq, Pageable pageable) {
		final Long userSeq = authUtils.getLoginUserSeq();
		return challengeBoardRepository.findAllBoardPage(cursorSeq, userSeq, challengeSeq, pageable).getContent();
	}

	@Transactional
	public void deleteBoard(Long challengeSeq, Long boardSeq) {
		challengeRepository.findById(challengeSeq).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
		challengeBoardRepository.deleteById(boardSeq);
	}

	@Transactional
	public void createChallenge(ChallengeCreateDto challengeCreateDto, MultipartFile image) {
		final User user = authUtils.getLoginUser();
		final LocalDate nowDate = LocalDate.now();

		if (challengeCreateDto.getDateStart().isBefore(nowDate)) {
			throw new CustomException(CHALLENGE_DATE_START_IS_BEFORE_NOW);
		}

		if (challengeCreateDto.getDateEnd().isBefore(challengeCreateDto.getDateStart())) {
			throw new CustomException(CHALLENGE_DATE_END_IS_BEFORE_DATE_START);
		}

		final Image savedImage = imageIsEmpty(image);

		final Challenge challenge = Challenge.builder()
			.manager(user)
			.image(savedImage)
			.name(challengeCreateDto.getName())
			.description(challengeCreateDto.getDescription())
			.goalDays(challengeCreateDto.getGoalDays())
			.goalType(challengeCreateDto.getGoalType())
			.goalAmount(challengeCreateDto.getGoalAmount())
			.dateStart(challengeCreateDto.getDateStart())
			.dateEnd(challengeCreateDto.getDateEnd())
			.password(challengeCreateDto.getPassword())
			.cost(challengeCreateDto.getCost())
			.nowMember(1L)
			.maxMember(challengeCreateDto.getMaxMember())
			.deleteYn('N')
			.build();

		challengeRepository.save(challenge);
		createTotalRecord(user.getSeq(), challenge.getSeq());
		challengeUserRepository.save(new ChallengeUser(user, challenge));
	}

	@Transactional
	public ChallengeDetailResponseDto getChallengeData(Long challengeSeq) {
		final Long userSeq = authUtils.getLoginUserSeq();
		final LocalDate localDate = LocalDate.now();
		final ChallengeDetailResponseDto challenge = challengeRepository.findChallengeBySeq(userSeq, challengeSeq, localDate)
				.orElseThrow(() -> new CustomException(CHALLENGE_NOT_FOUND));

		if (challenge.getDeleteYn() == 'Y') {
			throw new CustomException(CHALLENGE_NOT_FOUND);
		}

		return challenge;
	}

	@Transactional
	public boolean joinChallengeUser(Long challengeSeq, String password) {
		final User user = authUtils.getLoginUser();

		if (challengeUserRepository.existsByUserSeqAndChallengeSeq(user.getSeq(), challengeSeq)) {
			throw new CustomException(CHALLENGE_JOIN_ALREADY_EXIST);
		}
		final Challenge challenge = challengeRepository.findById(challengeSeq)
			.orElseThrow(() -> new CustomException(CHALLENGE_NOT_FOUND));

		if (challenge.getDeleteYn() == 'Y') {
			throw new CustomException(CHALLENGE_NOT_FOUND);
		}

		if (!challenge.getPassword().equals(password)) {
			throw new CustomException(CHALLENGE_JOIN_PASSWORD_FAIL);
		}

		if (user.getPoint() < challenge.getCost()) {
			throw new CustomException(CHALLENGE_JOIN_NOT_ENOUGH_POINT);
		}

		if (challenge.getNowMember() >= challenge.getMaxMember()) {
			throw new CustomException(CHALLENGE_JOIN_MAX_MEMBER);
		}

		challengeUserRepository.save(new ChallengeUser(user, challenge));
		challenge.plusNowMember();

		createTotalRecord(user.getSeq(), challengeSeq);

//		user.minusPoint(challenge.getCost().intValue());
		return true;
	}

	@Transactional
	public boolean isChallengeUser(Long challengeSeq) {
		final Long userSeq = authUtils.getLoginUserSeq();
		return challengeUserRepository.existsByUserSeqAndChallengeSeq(userSeq, challengeSeq);
	}

	@Transactional
	public List<ChallengeResponseDto> getAllChallengeList(Long cursorSeq, Pageable pageable) {
		final Long userSeq = authUtils.getLoginUserSeq();
		return challengeRepository.findAllChallengePage(cursorSeq, userSeq, pageable).getContent();
	}

	@Transactional
	public List<ChallengeResponseDto> getRecruitChallengeList(Long cursorSeq, Pageable pageable) {
		final Long userSeq = authUtils.getLoginUserSeq();
		final LocalDate localDate = LocalDate.now();
		return challengeRepository.findRecruitChallengePage(cursorSeq, userSeq, localDate, pageable).getContent();
	}

	@Transactional
	public List<ChallengeResponseDto> getMyChallengeList(Long cursorSeq, Pageable pageable) {
		final Long userSeq = authUtils.getLoginUserSeq();
		return challengeRepository.findMyChallengePage(cursorSeq, userSeq, pageable).getContent();
	}

	@Transactional
	public List<ChallengeResponseDto> getMyRunningChallengeList(Long cursorSeq, Pageable pageable) {
		final Long userSeq = authUtils.getLoginUserSeq();
		final LocalDate localDate = LocalDate.now();
		return challengeRepository.findMyRunningChallengePage(cursorSeq, userSeq, localDate, pageable).getContent();
	}

	@Transactional
	public boolean boardWarn(Long boardSeq) {
		final User user = authUtils.getLoginUser();
		final ChallengeBoard challengeBoard = challengeBoardRepository.findById(boardSeq)
			.orElseThrow(() -> new CustomException(BOARD_NOT_FOUND));

		if (challengeBoardWarnRepository.existsByUserAndChallengeBoard(user, challengeBoard)) {
			throw new CustomException(WARN_BOARD_ALREADY_EXIST);
		}

		final ChallengeBoardWarn challengeBoardWarn = new ChallengeBoardWarn(user, challengeBoard);
		challengeBoardWarnRepository.save(challengeBoardWarn);

		return true;
	}

	public Image imageIsEmpty(MultipartFile image) {
		if (image == null) {
			return ImageCache.get(ImageCache.DEFAULT_CHALLENGE);
		} else if (image.isEmpty()) {
			return ImageCache.get(ImageCache.DEFAULT_CHALLENGE);
		}
		return imageService.save(image);
	}
	@Transactional
	public Resource getChallengeImage(Long challengeSeq) {
		final Challenge challenge = challengeRepository.findById(challengeSeq)
				.orElseThrow(() -> new CustomException(CHALLENGE_NOT_FOUND));
		return imageService.getImage(challenge.getImage().getSeq());
	}

	public List<ChallengeEndDto> findByDateEndIsToday() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		return challengeRepository.findByDateEndIsToday(LocalDate.parse(sdf.format(today)));
	}

	public int getChallengeCount(Long challengeSeq) {
		return challengeRepository.getChallengeCount(challengeSeq);
	}

	@Transactional
	public boolean deleteMyChallenge(Long challengeSeq) {
		final User user = authUtils.getLoginUser();

		if (!challengeUserRepository.existsByUserSeqAndChallengeSeq(user.getSeq(), challengeSeq)) {
			throw new CustomException(CHALLENGE_NOT_JOIN);
		}

		final Challenge challenge = challengeRepository.findById(challengeSeq)
			.orElseThrow(() -> new CustomException(USER_NOT_FOUND));

		boolean flag = false;
		if (challenge.getManager().getSeq().equals(user.getSeq())){
			challenge.deleteChallenge();
//			challenge.minusNowMember();
			flag = true;
		}

		challengeUserRepository.deleteByUserSeqAndChallengeSeq(user.getSeq(), challengeSeq);
		challenge.minusNowMember();
		user.addPoint(challenge.getCost().intValue());
		
		return flag;
	}

	public void createTotalRecord(Long userSeq, Long challengeSeq) {
		final ChallengeTotalRecord challengeTotalRecord = ChallengeTotalRecord.builder()
			.userSeq(userSeq)
			.challengeSeq(challengeSeq)
			.build();
		challengeTotalRecordRepository.save(challengeTotalRecord);
	}
}
