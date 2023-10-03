package com.runwithme.runwithme.domain.challenge.service;

import static com.runwithme.runwithme.global.result.ResultCode.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.runwithme.runwithme.domain.challenge.dto.ChallengeBoardPostDto;
import com.runwithme.runwithme.domain.challenge.dto.ChallengeBoardResponseDto;
import com.runwithme.runwithme.domain.challenge.dto.ChallengeCreateDto;
import com.runwithme.runwithme.domain.challenge.dto.ChallengeEndDto;
import com.runwithme.runwithme.domain.challenge.dto.ChallengeResponseDto;
import com.runwithme.runwithme.domain.challenge.entity.Challenge;
import com.runwithme.runwithme.domain.challenge.entity.ChallengeBoard;
import com.runwithme.runwithme.domain.challenge.entity.ChallengeBoardWarn;
import com.runwithme.runwithme.domain.challenge.entity.ChallengeUser;
import com.runwithme.runwithme.domain.challenge.repository.ChallengeBoardRepository;
import com.runwithme.runwithme.domain.challenge.repository.ChallengeBoardWarnRepository;
import com.runwithme.runwithme.domain.challenge.repository.ChallengeRepository;
import com.runwithme.runwithme.domain.challenge.repository.ChallengeUserRepository;
import com.runwithme.runwithme.domain.user.entity.User;
import com.runwithme.runwithme.global.dto.PagingResultDto;
import com.runwithme.runwithme.global.entity.Image;
import com.runwithme.runwithme.global.error.CustomException;
import com.runwithme.runwithme.global.service.ImageService;
import com.runwithme.runwithme.global.utils.AuthUtils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.runwithme.runwithme.global.result.ResultCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChallengeService {
	private final ChallengeRepository challengeRepository;
	private final ChallengeBoardRepository challengeBoardRepository;
	private final ChallengeUserRepository challengeUserRepository;
	private final ChallengeBoardWarnRepository challengeBoardWarnRepository;
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
    public List<ChallengeBoardResponseDto> getBoardList(Long cursorSeq, Long challengeSeq, Pageable pageable){
        final Long userSeq = authUtils.getLoginUserSeq();
        return challengeBoardRepository.findAllBoardPage(cursorSeq, userSeq, challengeSeq, pageable).getContent();
    }

	@Transactional
	public void deleteBoard(Long boardSeq) {
		challengeBoardRepository.deleteById(boardSeq);
	}

    @Transactional
    public void createChallenge(ChallengeCreateDto challengeCreateDto, MultipartFile image) {
        final User user = authUtils.getLoginUser();
        final LocalDate nowDate = LocalDate.now();

        if (challengeCreateDto.getDateStart().isBefore(nowDate)){
            throw new CustomException(CHALLENGE_DATE_START_IS_BEFORE_NOW);
        }

        if (challengeCreateDto.getDateEnd().isBefore(challengeCreateDto.getDateStart())){
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
                .build();

        challengeRepository.save(challenge);

        challengeUserRepository.save(new ChallengeUser(user, challenge));
    }

    @Transactional
    public Optional<ChallengeResponseDto> getChallengeData(Long challengeSeq) {
        final Long userSeq = authUtils.getLoginUserSeq();

        if (!challengeRepository.existsById(challengeSeq)) {
            throw new CustomException(CHALLENGE_NOT_FOUND);
        }

        return challengeRepository.findChallengeBySeq(userSeq, challengeSeq);
    }

	@Transactional
	public boolean joinChallengeUser(Long challengeSeq, String password) {
		final User user = authUtils.getLoginUser();

        if (challengeUserRepository.existsByUserSeqAndChallengeSeq(user.getSeq(), challengeSeq)) {
            throw new CustomException(CHALLENGE_JOIN_ALREADY_EXIST);
        }
        final Challenge challenge = challengeRepository.findById(challengeSeq).get();
        if (!challenge.getPassword().equals(password)){
            throw new CustomException(CHALLENGE_JOIN_PASSWORD_FAIL);
        }
        challengeUserRepository.save(new ChallengeUser(user, challenge));
        return true;
    }

    @Transactional
    public boolean isChallengeUser(Long challengeSeq) {
        final Long userSeq = authUtils.getLoginUserSeq();
        if (!challengeUserRepository.existsByUserSeqAndChallengeSeq(userSeq, challengeSeq)) {
            return false;
        }
        return true;
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
		if (image.isEmpty()) return null;
		return imageService.save(image);
	}

	public List<ChallengeEndDto> findByDateEndIsToday() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		return challengeRepository.findByDateEndIsToday(LocalDate.parse(sdf.format(today)));
	}

	public int getChallengeCount(Long challengeSeq) {
		return challengeRepository.getChallengeCount(challengeSeq);
	}

	public void deleteMyChallenge(Long challengeSeq) {
		challengeRepository.deleteMyChallenge(challengeSeq);
	}
}
