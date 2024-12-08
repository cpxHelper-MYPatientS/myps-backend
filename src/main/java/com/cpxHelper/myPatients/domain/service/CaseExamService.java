package com.cpxHelper.myPatients.domain.service;

import com.cpxHelper.myPatients.domain.entity.caseexam.CaseExam;
import com.cpxHelper.myPatients.domain.repository.CaseExamRepository;
import com.cpxHelper.myPatients.domain.entity.member.Member;
import com.cpxHelper.myPatients.domain.entity.patient.Patient;
import com.cpxHelper.myPatients.domain.repository.MemberRepository;
import com.cpxHelper.myPatients.domain.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CaseExamService {

    private final CaseExamRepository caseExamRepository;
    private final PatientRepository patientRepository;
    private final MemberRepository memberRepository;;

    // 시험 생성
    public CaseExam createCaseExam(Long memberId, Long patientId) {

        // 멤버 객체 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 멤버를 찾을 수 없습니다. 멤버 ID: " + memberId));

        // 환자 객체 조회 (환자 ID를 기반으로 환자 객체 가져오기)
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 환자를 찾을 수 없습니다. 환자 ID: " + patientId));

        // 새로운 CaseExam 생성
        CaseExam caseExam = CaseExam.builder()
                .member(member)
                .patient(patient) // 환자 객체 설정
                .createdAt(LocalDateTime.now())
                .build();

        return caseExamRepository.save(caseExam);
    }

    // 사례 시험 ID로 조회
    @Transactional(readOnly = true)
    public CaseExam getCaseExamById(Long caseExamId) {
        return caseExamRepository.findById(caseExamId)
                .orElseThrow(() -> new IllegalArgumentException("CaseExam not found with ID: " + caseExamId));
    }

    // 특정 작성자(Member)의 사례 시험 조회
    @Transactional(readOnly = true)
    public List<CaseExam> getCaseExamsByMember(Long memberId) {
        return caseExamRepository.findByMemberId(memberId);
    }

    // 특정 환자(Patient)의 사례 시험 조회
    @Transactional(readOnly = true)
    public List<CaseExam> getCaseExamsByPatient(Long patientId) {
        return caseExamRepository.findByPatientId(patientId);
    }

    // 특정 시간 범위 내의 사례 시험 조회
    @Transactional(readOnly = true)
    public List<CaseExam> getCaseExamsByDateRange(LocalDateTime start, LocalDateTime end) {
        return caseExamRepository.findByCreatedAtBetween(start, end);
    }

    // 사례 시험 삭제
    public void deleteCaseExam(Long caseExamId) {
        CaseExam caseExam = getCaseExamById(caseExamId);
        caseExamRepository.delete(caseExam);
    }
}
