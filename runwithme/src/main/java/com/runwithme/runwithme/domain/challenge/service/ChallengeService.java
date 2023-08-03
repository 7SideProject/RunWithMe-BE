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
<<<<<<< HEAD
import com.runwithme.runwithme.global.error.CustomException;
=======
import com.runwithme.runwithme.global.entity.Image;
import com.runwithme.runwithme.global.error.exception.EntityAlreadyExistException;

import com.runwithme.runwithme.global.service.ImageService;
import com.runwithme.runwithme.global.utils.AuthUtils;
>>>>>>> develop
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.runwithme.runwithme.global.result.ResultCode.*;

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
<<<<<<< HEAD
    public void createBoard(Long challengeSeq, ChallengeBoardPostDto challengeBoardPostDto) {
        final Long userSeq = 1L;
        final User user = userRepository.findById(userSeq).get();
=======
    public void createBoard(Long challengeSeq, ChallengeBoardPostDto challengeBoardPostDto){
        final User user = authUtils.getLoginUser();

>>>>>>> develop
        final LocalDateTime challengeBoardRegTime = LocalDateTime.now();
        final ChallengeBoard challengeBoard = ChallengeBoard.builder()
                .user(user)
                .challengeSeq(challengeSeq)
                .imgSeq(challengeBoardPostDto.getImgSeq())
                .challengeBoardContent(challengeBoardPostDto.getChallengeBoardContent())
                .challengeBoardRegTime(challengeBoardRegTime).build();
        challengeBoardRepository.save(challengeBoard);
    }

    @Transactional
<<<<<<< HEAD
    public PagingResultDto<ChallengeBoardResponseDto> getBoardList(Long challengeSeq, Pageable pageable) {
=======
    public PagingResultDto getBoardList(Long challengeSeq, Pageable pageable){
        final User user = authUtils.getLoginUser();

>>>>>>> develop
        final Page<ChallengeBoardResponseDto> allBoards = challengeBoardRepository.findAllBoardPage(challengeSeq, pageable);
        return new PagingResultDto<>(pageable.getPageNumber(), allBoards.getTotalPages() - 1, allBoards.getContent());
    }

    @Transactional
<<<<<<< HEAD
    public void deleteBoard(Long boardSeq) {
=======
    public void deleteBoard(Long boardSeq){
        final User user = authUtils.getLoginUser();

>>>>>>> develop
        challengeBoardRepository.deleteById(boardSeq);
    }

    @Transactional
<<<<<<< HEAD
    public void createChallenge(ChallengeCreateDto challengeCreateDto) {
        final Long userSeq = 1L;
=======
    public void createChallenge(ChallengeCreateDto challengeCreateDto, ChallengeImageDto imgFile) throws IOException {
        final User user = authUtils.getLoginUser();

        final Image savedImage = imageService.save(imgFile.image());

>>>>>>> develop
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
<<<<<<< HEAD
                .cost(challengeCreateDto.getCost()).build();
=======
                .cost(challengeCreateDto.getCost())
                .nowMember(challengeCreateDto.getNowMember())
                .maxMember(challengeCreateDto.getMaxMember())
                .build();

>>>>>>> develop
        challengeRepository.save(challenge);
    }

    @Transactional
<<<<<<< HEAD
    public Challenge getChallengeData(Long challengeSeq) {
        return challengeRepository.findById(challengeSeq).get();
    }

    @Transactional
    public boolean joinChallengeUser(Long challengeSeq, String password) {
        final Long userSeq = 1L;
        if (challengeUserRepository.existsByUserSeqAndChallengeSeq(userSeq, challengeSeq)) {
            throw new CustomException(CHALLENGE_JOIN_ALREADY_EXIST);
=======
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
>>>>>>> develop
        }
        final Challenge challenge = challengeRepository.findById(challengeSeq).get();
<<<<<<< HEAD
        if (Objects.equals(challenge.getPassword(), password)) {
            challengeUserRepository.save(new ChallengeUser(userSeq, challenge));
=======

        if (challenge.getPassword() == password) {
            challengeUserRepository.save(new ChallengeUser(user, challenge));
>>>>>>> develop
            return true;
        }
        return false;
    }

    @Transactional
<<<<<<< HEAD
    public boolean isChallengeUser(Long challengeSeq) {
        final Long userSeq = 1L;
        if (challengeUserRepository.existsByUserSeqAndChallengeSeq(userSeq, challengeSeq)) {
            throw new CustomException(CHALLENGE_JOIN_ALREADY_EXIST);
=======
    public boolean isChallengeUser(Long challengeSeq){
        final Long userSeq = authUtils.getLoginUserSeq();

        if (challengeUserRepository.existsByUserSeqAndChallengeSeq(userSeq, challengeSeq)){
            throw new EntityAlreadyExistException(CHALLENGE_JOIN_ALREADY_EXIST);
>>>>>>> develop
        }
        return true;
    }

    @Transactional
<<<<<<< HEAD
    public PagingResultDto<ChallengeResponseDto> getAllChallengeList(Pageable pageable) {
        final Long userSeq = 1L;
=======
    public PagingResultDto getAllChallengeList(Pageable pageable){
        final Long userSeq = authUtils.getLoginUserSeq();
        final LocalDateTime localDateTime = LocalDateTime.now();
>>>>>>> develop
        final Page<ChallengeResponseDto> challenges = challengeRepository.findAllChallengePage(userSeq, pageable);
        return new PagingResultDto<>(pageable.getPageNumber(), challenges.getTotalPages() - 1, challenges.getContent());
    }

    @Transactional
<<<<<<< HEAD
    public PagingResultDto<ChallengeResponseDto> getMyChallengeList(Pageable pageable) {
        final Long userSeq = 1L;
=======
    public PagingResultDto getRecruitChallengeList(Pageable pageable){
        final Long userSeq = authUtils.getLoginUserSeq();
        final LocalDateTime localDateTime = LocalDateTime.now();
        final Page<ChallengeResponseDto> challenges = challengeRepository.findRecruitChallengePage(userSeq, localDateTime, pageable);
        final PagingResultDto<ChallengeResponseDto> pagingResultDto = new PagingResultDto(pageable.getPageNumber(), challenges.getTotalPages() - 1, challenges.getContent());

        return pagingResultDto;
    }

    @Transactional
    public PagingResultDto getMyChallengeList(Pageable pageable){
        final Long userSeq = authUtils.getLoginUserSeq();
>>>>>>> develop
        final Page<ChallengeResponseDto> challenges = challengeRepository.findMyChallengePage(userSeq, pageable);
        return new PagingResultDto<>(pageable.getPageNumber(), challenges.getTotalPages() - 1, challenges.getContent());
    }
}
