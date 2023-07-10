package com.runwithme.runwithme.domain.challenge.service;

import com.runwithme.runwithme.domain.challenge.dto.ChallengeBoardPostDto;
import com.runwithme.runwithme.domain.challenge.dto.ChallengeBoardResponseDto;
import com.runwithme.runwithme.domain.challenge.dto.ChallengeCreateDto;
import com.runwithme.runwithme.domain.challenge.dto.ChallengeResponseDto;
import com.runwithme.runwithme.domain.challenge.entity.Challenge;
import com.runwithme.runwithme.domain.challenge.entity.ChallengeBoard;
import com.runwithme.runwithme.domain.challenge.entity.ChallengeUser;
import com.runwithme.runwithme.domain.challenge.repository.ChallengeBoardRepository;
import com.runwithme.runwithme.domain.challenge.repository.ChallengeRepository;
import com.runwithme.runwithme.domain.challenge.repository.ChallengeUserRepository;
import com.runwithme.runwithme.domain.user.entity.User;
import com.runwithme.runwithme.domain.user.repository.UserRepository;
import com.runwithme.runwithme.global.dto.PagingResultDto;
import com.runwithme.runwithme.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.runwithme.runwithme.global.result.ResultCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeBoardRepository challengeBoardRepository;
    private final ChallengeUserRepository challengeUserRepository;
    private final UserRepository userRepository;

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
    public void createChallenge(ChallengeCreateDto challengeCreateDto){
        final Long userSeq = new Long(1);

        final Challenge challenge = Challenge.builder()
                .managerSeq(userSeq)
                .imgSeq(challengeCreateDto.getImgSeq())
                .name(challengeCreateDto.getName())
                .description(challengeCreateDto.getDescription())
                .goalDays(challengeCreateDto.getGoalDays())
                .goalType(challengeCreateDto.getGoalType())
                .goalAmount(challengeCreateDto.getGoalAmount())
                .dateStart(challengeCreateDto.getDateStart())
                .dateEnd(challengeCreateDto.getDateEnd())
                .timeStart(challengeCreateDto.getTimeStart())
                .timeEnd(challengeCreateDto.getTimeEnd())
                .password(challengeCreateDto.getPassword())
                .cost(challengeCreateDto.getCost()).build();

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
            throw new CustomException(CHALLENGE_JOIN_ALREADY_EXIST);
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
            throw new CustomException(CHALLENGE_JOIN_ALREADY_EXIST);
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
