package com.runwithme.runwithme.domain.challenge.service;

import com.runwithme.runwithme.domain.challenge.dto.*;
import com.runwithme.runwithme.domain.challenge.entity.Challenge;
import com.runwithme.runwithme.domain.challenge.entity.ChallengeBoard;
import com.runwithme.runwithme.domain.challenge.entity.ChallengeBoardWarn;
import com.runwithme.runwithme.domain.challenge.entity.ChallengeUser;
import com.runwithme.runwithme.domain.challenge.repository.ChallengeBoardRepository;
import com.runwithme.runwithme.domain.challenge.repository.ChallengeBoardWarnRepository;
import com.runwithme.runwithme.domain.challenge.repository.ChallengeRepository;
import com.runwithme.runwithme.domain.challenge.repository.ChallengeUserRepository;
import com.runwithme.runwithme.domain.user.entity.User;
import com.runwithme.runwithme.domain.user.repository.UserRepository;
import com.runwithme.runwithme.global.dto.PagingResultDto;
import com.runwithme.runwithme.global.error.CustomException;
import com.runwithme.runwithme.global.entity.Image;
import com.runwithme.runwithme.global.repository.ImageRepository;
import com.runwithme.runwithme.global.service.ImageService;
import com.runwithme.runwithme.global.utils.AuthUtils;
import jakarta.persistence.EntityNotFoundException;
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
    private final ImageRepository imageRepository;

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
    public void deleteBoard(Long boardSeq){
        final User user = authUtils.getLoginUser();
        challengeBoardRepository.deleteById(boardSeq);
    }

    @Transactional
    public void createChallenge(ChallengeCreateDto challengeCreateDto, MultipartFile image) {
        final User user = authUtils.getLoginUser();

        final Image savedImage = imageIsEmpty(image);

        final Challenge challenge = Challenge.builder()
                .manager(user)
                .image(savedImage)
                .name(challengeCreateDto.getName())
                .description(challengeCreateDto.getDescription())
                .goalDays(challengeCreateDto.getGoalDays())
                .goalType(challengeCreateDto.getGoalType())
                .goalAmount(challengeCreateDto.getGoalAmount())
                .timeStart(challengeCreateDto.getTimeStart())
                .timeEnd(challengeCreateDto.getTimeEnd())
                .password(challengeCreateDto.getPassword())
                .cost(challengeCreateDto.getCost())
                .nowMember(1L)
                .maxMember(challengeCreateDto.getMaxMember())
                .build();

        challengeRepository.save(challenge);
    }

    @Transactional
    public Optional<ChallengeResponseDto> getChallengeData(Long challengeSeq) {
        final Long userSeq = authUtils.getLoginUserSeq();

        return challengeRepository.findChallengeBySeq(userSeq, challengeSeq);
    }

    @Transactional
    public boolean joinChallengeUser(Long challengeSeq, String password) {
        final User user = authUtils.getLoginUser();

        if (challengeUserRepository.existsByUserSeqAndChallengeSeq(user.getSeq(), challengeSeq)) {
            throw new CustomException(CHALLENGE_JOIN_ALREADY_EXIST);
        }
        final Challenge challenge = challengeRepository.findById(challengeSeq).get();
        if (Objects.equals(challenge.getPassword(), password)) {
            challengeUserRepository.save(new ChallengeUser(user, challenge));
            return true;
        }

        return false;
    }

    @Transactional
    public boolean isChallengeUser(Long challengeSeq) {
        final Long userSeq = authUtils.getLoginUserSeq();
        if (challengeUserRepository.existsByUserSeqAndChallengeSeq(userSeq, challengeSeq)) {
            throw new CustomException(CHALLENGE_JOIN_ALREADY_EXIST);
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
                .orElseThrow(()->new CustomException(BOARD_NOT_FOUND));

        if(challengeBoardWarnRepository.existsByUserAndChallengeBoard(user, challengeBoard)) {
            throw new CustomException(WARN_BOARD_ALREADY_EXIST);
        }

        final ChallengeBoardWarn challengeBoardWarn = new ChallengeBoardWarn(user, challengeBoard);
        challengeBoardWarnRepository.save(challengeBoardWarn);

        return true;
    }

    public Image imageIsEmpty(MultipartFile image) {
        if (image.isEmpty()) {
            return null;
        } else {
            return imageService.save(image);
        }
    }
}
