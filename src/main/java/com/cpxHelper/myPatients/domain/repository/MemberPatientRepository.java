package com.cpxHelper.myPatients.domain.repository;

import com.cpxHelper.myPatients.domain.entity.member.Member;
import com.cpxHelper.myPatients.domain.entity.member.MemberPatient;
import com.cpxHelper.myPatients.domain.entity.patient.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberPatientRepository extends JpaRepository<MemberPatient, Long> {

    // 특정 멤버에 연결된 모든 환자 조회
    List<MemberPatient> findByMember(Member member);

    // 특정 환자와 연결된 모든 멤버 조회
    List<MemberPatient> findByPatient(Patient patient);

    // 특정 멤버와 환자 간의 매핑 조회
    MemberPatient findByMemberAndPatient(Member member, Patient patient);
}
