package com.cpxHelper.myPatients.domain.service;

import com.cpxHelper.myPatients.domain.entity.member.Member;
import com.cpxHelper.myPatients.domain.service.NotificationService;
import com.cpxHelper.myPatients.domain.entity.patient.Patient;
import com.cpxHelper.myPatients.domain.entity.member.MemberPatient;
import com.cpxHelper.myPatients.domain.repository.MemberPatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberPatientService {

    private final MemberPatientRepository userPatientRepository;
    private final NotificationService notificationService;

    public MemberPatient addPatientToMember(Member member, Patient patient) {
        // UserPatient 매핑 엔티티 생성 및 저장
        MemberPatient userPatient = MemberPatient.builder()
                .member(member)
                .patient(patient)
                .build();
        userPatientRepository.save(userPatient);

        // 알림 생성
//        String message = String.format("환자 %s님이 추가되었습니다.", patient.getName());
//        notificationService.createNotification(member, message);

        return userPatient;
    }
}
