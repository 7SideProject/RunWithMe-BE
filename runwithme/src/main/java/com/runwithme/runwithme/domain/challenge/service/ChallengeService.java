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

    @Transactional
    public void createBoard(Long challengeSeq, ChallengeBoardPostDto challengeBoardPostDto){
        final Long userSeq = new Long(1);

        final User user = userRepository.findById(userSeq).get();

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
    public PagingResultDto getBoardList(Long challengeSeq, Pageable pageable){
        final Page<ChallengeBoardResponseDto> allBoards = challengeBoardRepository.findAllBoardPage(challengeSeq, pageable);
        final PagingResultDto<ChallengeBoardResponseDto> pagingResultDto = new PagingResultDto(pageable.getPageNumber(), allBoards.getTotalPages() - 1, allBoards.getContent());
        return pagingResultDto;
    }

    @Transactional
    public void deleteBoard(Long boardSeq){
        challengeBoardRepository.deleteById(boardSeq);
    }

    @Transactional
    public void createChallenge(ChallengeCreateDto challengeCreateDto, ChallengeImageDto imgFile) throws IOException {
        final Long userSeq = new Long(1);
        final User user = userRepository.findById(userSeq).orElseThrow(IllegalArgumentException::new);

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
        final Long userSeq = new Long(1);

        final Challenge challenge = challengeRepository.findById(challengeSeq).get();

        return challenge;
    }

    @Transactional
    public boolean joinChallengeUser(Long challengeSeq, String password){
        final Long userSeq = new Long(1);

        if (challengeUserRepository.existsByUserSeqAndChallengeSeq(userSeq, challengeSeq)){
            throw new EntityAlreadyExistException(CHALLENGE_JOIN_ALREADY_EXIST);
        }

        final Challenge challenge = challengeRepository.findById(challengeSeq).get();

        if (challenge.getPassword() == password) {
            challengeUserRepository.save(new ChallengeUser(userSeq, challenge));
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public boolean isChallengeUser(Long challengeSeq){
        final Long userSeq = new Long(1);

        if (challengeUserRepository.existsByUserSeqAndChallengeSeq(userSeq, challengeSeq)){
            throw new EntityAlreadyExistException(CHALLENGE_JOIN_ALREADY_EXIST);
        }

        return true;
    }

    @Transactional
    public PagingResultDto getAllChallengeList(Pageable pageable){
        final Long userSeq = new Long(1);
        final Page<ChallengeResponseDto> challenges = challengeRepository.findAllChallengePage(userSeq, pageable);
        final PagingResultDto<ChallengeResponseDto> pagingResultDto = new PagingResultDto(pageable.getPageNumber(), challenges.getTotalPages() - 1, challenges.getContent());

        return pagingResultDto;
    }

    @Transactional
    public PagingResultDto getMyChallengeList(Pageable pageable){
        final Long userSeq = new Long(1);
        final Page<ChallengeResponseDto> challenges = challengeRepository.findMyChallengePage(userSeq, pageable);
        final PagingResultDto<ChallengeResponseDto> pagingResultDto = new PagingResultDto(pageable.getPageNumber(), challenges.getTotalPages() - 1, challenges.getContent());

        return pagingResultDto;
    }
}
