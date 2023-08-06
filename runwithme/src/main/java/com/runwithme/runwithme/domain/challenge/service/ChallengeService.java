package com.runwithme.runwithme.domain.challenge.service;

import com.runwithme.runwithme.domain.challenge.dto.*;
import com.runwithme.runwithme.domain.challenge.entity.Challenge;
import com.runwithme.runwithme.domain.challenge.entity.ChallengeBoard;
import com.runwithme.runwithme.domain.challenge.entity.ChallengeUser;
import com.runwithme.runwithme.domain.challenge.repository.ChallengeBoardRepository;
import com.runwithme.runwithme.domain.challenge.repository.ChallengeRepository;
import com.runwithme.runwithme.domain.challenge.repository.ChallengeUserRepository;
import com.runwithme.runwithme.domain.user.entity.User;
import com.runwithme.runwithme.domain.user.repository.UserRepository;
import com.runwithme.runwithme.global.dto.PagingResultDto;
import com.runwithme.runwithme.global.entity.Image;
import com.runwithme.runwithme.global.error.exception.EntityAlreadyExistException;

import com.runwithme.runwithme.global.service.ImageService;
import com.runwithme.runwithme.global.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;

import static com.runwithme.runwithme.global.error.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChallengeService {
    private final ChallengeRepository challengeRepository;
    private final ChallengeBoardRepository challengeBoardRepository;
    private final ChallengeUserRepository challengeUserRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;

    private final AuthUtils authUtils;

    @Transactional
    public void createBoard(Long challengeSeq, ChallengeBoardPostDto challengeBoardPostDto, ChallengeImageDto imgFile) throws IOException {
        final User user = authUtils.getLoginUser();

        final Image savedImage = imageService.save(imgFile.image());

        final LocalDateTime challengeBoardRegTime = LocalDateTime.now();
        final ChallengeBoard challengeBoard = ChallengeBoard.builder()
                .user(user)
                .challengeSeq(challengeSeq)
                .image(savedImage)
                .challengeBoardContent(challengeBoardPostDto.getChallengeBoardContent())
                .challengeBoardRegTime(challengeBoardRegTime).build();
        challengeBoardRepository.save(challengeBoard);
    }

    @Transactional
    public PagingResultDto getBoardList(Long cursorSeq, Long challengeSeq, Pageable pageable){
        final User user = authUtils.getLoginUser();

        final Page<ChallengeBoardResponseDto> allBoards = challengeBoardRepository.findAllBoardPage(cursorSeq, challengeSeq, pageable);
        final PagingResultDto<ChallengeBoardResponseDto> pagingResultDto = new PagingResultDto(pageable.getPageNumber(), allBoards.getTotalPages() - 1, allBoards.getContent());
        return pagingResultDto;
    }

    @Transactional
    public void deleteBoard(Long boardSeq){
        final User user = authUtils.getLoginUser();

        challengeBoardRepository.deleteById(boardSeq);
    }

    @Transactional
    public void createChallenge(ChallengeCreateDto challengeCreateDto, ChallengeImageDto imgFile) throws IOException {
        final User user = authUtils.getLoginUser();

        final Image savedImage = imageService.save(imgFile.image());

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
                .nowMember(challengeCreateDto.getNowMember())
                .maxMember(challengeCreateDto.getMaxMember())
                .build();

        challengeRepository.save(challenge);
    }

    @Transactional
    public Challenge getChallengeData(Long challengeSeq){
        final User user = authUtils.getLoginUser();

        final Challenge challenge = challengeRepository.findById(challengeSeq).get();

        return challenge;
    }

    @Transactional
    public boolean joinChallengeUser(Long challengeSeq, String password){
        final User user = authUtils.getLoginUser();

        if (challengeUserRepository.existsByUserSeqAndChallengeSeq(user.getSeq(), challengeSeq)){
            throw new EntityAlreadyExistException(CHALLENGE_JOIN_ALREADY_EXIST);
        }

        final Challenge challenge = challengeRepository.findById(challengeSeq).get();

        if (challenge.getPassword() == password) {
            challengeUserRepository.save(new ChallengeUser(user, challenge));
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public boolean isChallengeUser(Long challengeSeq){
        final Long userSeq = authUtils.getLoginUserSeq();

        if (challengeUserRepository.existsByUserSeqAndChallengeSeq(userSeq, challengeSeq)){
            throw new EntityAlreadyExistException(CHALLENGE_JOIN_ALREADY_EXIST);
        }

        return true;
    }

    @Transactional
    public PagingResultDto getAllChallengeList(Long cursorSeq, Pageable pageable){
        final Long userSeq = authUtils.getLoginUserSeq();
        final LocalDateTime localDateTime = LocalDateTime.now();
        final Page<ChallengeResponseDto> challenges = challengeRepository.findAllChallengePage(cursorSeq, userSeq, pageable);
        final PagingResultDto<ChallengeResponseDto> pagingResultDto = new PagingResultDto(pageable.getPageNumber(), challenges.getTotalPages() - 1, challenges.getContent());

        return pagingResultDto;
    }

    @Transactional
    public PagingResultDto getRecruitChallengeList(Long cursorSeq, Pageable pageable){
        final Long userSeq = authUtils.getLoginUserSeq();
        final LocalDateTime localDateTime = LocalDateTime.now();
        final Page<ChallengeResponseDto> challenges = challengeRepository.findRecruitChallengePage(cursorSeq, userSeq, localDateTime, pageable);
        final PagingResultDto<ChallengeResponseDto> pagingResultDto = new PagingResultDto(pageable.getPageNumber(), challenges.getTotalPages() - 1, challenges.getContent());

        return pagingResultDto;
    }

    @Transactional
    public PagingResultDto getMyChallengeList(Long cursorSeq, Pageable pageable){
        final Long userSeq = authUtils.getLoginUserSeq();
        final Page<ChallengeResponseDto> challenges = challengeRepository.findMyChallengePage(cursorSeq, userSeq, pageable);
        final PagingResultDto<ChallengeResponseDto> pagingResultDto = new PagingResultDto(pageable.getPageNumber(), challenges.getTotalPages() - 1, challenges.getContent());

        return pagingResultDto;
    }
}
