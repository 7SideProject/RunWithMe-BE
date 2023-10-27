package com.runwithme.runwithme.domain.user.facade;

import com.runwithme.runwithme.domain.record.dto.ChallengeTotalRecordResponseDto;
import com.runwithme.runwithme.domain.record.service.RecordService;
import com.runwithme.runwithme.domain.user.dto.UserTotalRecordViewDto;
import com.runwithme.runwithme.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;
    private final RecordService recordService;

    @Transactional(readOnly = true)
    public UserTotalRecordViewDto getUserTotalRecord(Long userSeq) {
        userService.getUserProfile(userSeq);
        ChallengeTotalRecordResponseDto userTotalRecord = recordService.getUserTotalRecord(userSeq);
        return new UserTotalRecordViewDto(userSeq, userTotalRecord);
    }
}
