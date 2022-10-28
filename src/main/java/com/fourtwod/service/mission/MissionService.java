package com.fourtwod.service.mission;

import com.fourtwod.config.auth.dto.SessionUser;
import com.fourtwod.domain.mission.*;
import com.fourtwod.domain.user.QUser;
import com.fourtwod.domain.user.User;
import com.fourtwod.domain.user.UserId;
import com.fourtwod.web.handler.ResponseCode;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MissionService {

    private final MissionRepository missionRepository;
    private final MissionDetailRepository missionDetailRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Transactional
    public List<MissionDetail> selectMissionInProgress(SessionUser sessionUser) {
        QUser user = QUser.user;
        QMission mission = QMission.mission;
        QMissionDetail missionDetail = QMissionDetail.missionDetail;

        List<MissionDetail> list = jpaQueryFactory.selectFrom(missionDetail)
                .innerJoin(mission)
                    .on(missionDetail.missionDetailId.missionDetailId.eq(mission.missionId))
                .rightJoin(user)
                    .on(mission.user.userId.eq(user.userId))
                .where(user.userId.email.eq(sessionUser.getEmail())
                        .and(user.userId.registrationId.eq(sessionUser.getRegistrationId())))
                .fetch();

        return list;
    }

    @Transactional
    public ResponseCode createMission(int missionType, SessionUser user) {
        // 미션 타입 체크
        switch (missionType) {
            case 1: case 3: case 5: case 7: break;
            default: return ResponseCode.MISS_E000;
        }

        // 미션 생성
        Mission mission = missionRepository.save(Mission.builder()
                .missionType(missionType)
                .user(User.builder()
                        .userId(UserId.builder()
                                .email(user.getEmail())
                                .registrationId(user.getRegistrationId())
                                .build())
                        .build())
                .build());

        LocalDate localDate = LocalDate.now();

        // 미션 디테일 생성
        for (int i = 0; i < missionType; i++) {
            String strLocalDate = localDate.plusDays(i).format(DateTimeFormatter.BASIC_ISO_DATE);
            MissionDetail missionDetail = missionDetailRepository.save(MissionDetail.builder()
                    .missionDetailId(MissionDetailId.builder()
                            .missionDetailId(mission.getMissionId())
                            .date(strLocalDate)
                            .build())
                    .afternoon(0)
                    .night(0)
                    .mission(mission)
                    .build());
        }

        return ResponseCode.COMM_S000;
    }
}
