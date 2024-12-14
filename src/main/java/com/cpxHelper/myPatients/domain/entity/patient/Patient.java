package com.cpxHelper.myPatients.domain.entity.patient;

import com.cpxHelper.myPatients.domain.entity.chiefcomplaints.ChiefComplaints;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "patient_tb") // 테이블 이름 지정
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "gender", nullable = false)
    private String gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chief_complaints_id", nullable = false)
    private ChiefComplaints chiefComplaints;

    @Column(name = "extra_info", columnDefinition = "TEXT")
    private String extraInfo; // JSON 문자열로 추가 데이터 저장

    @Transient // 데이터베이스에 저장하지 않고 엔티티에서만 사용
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Builder
    public Patient(String name, LocalDate dateOfBirth, Integer age, String gender, ChiefComplaints chiefComplaints, Map<String, Object> extraInfoMap) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.age = age;
        this.gender = gender;
        this.chiefComplaints = chiefComplaints;
        this.extraInfo = convertMapToJson(extraInfoMap);
    }

    // JSON 문자열을 Map으로 변환하는 메서드
    public Map<String, Object> getExtraInfoAsMap() {
        if (this.extraInfo == null || this.extraInfo.isEmpty()) {
            return Map.of();
        }
        try {
            return objectMapper.readValue(this.extraInfo, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse extraInfo JSON", e);
        }
    }

    // Map을 JSON 문자열로 변환하는 메서드
    private String convertMapToJson(Map<String, Object> extraInfoMap) {
        if (extraInfoMap == null || extraInfoMap.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(extraInfoMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert extraInfo Map to JSON", e);
        }
    }

    // 환자 정보를 JSON 형식으로 반환하는 메서드
    public String getPatientInfoAsJson() {
        try {
            Map<String, Object> patientInfo = new HashMap<>();
            patientInfo.put("Name", this.name);
            patientInfo.put("Date_of_Birth", this.dateOfBirth.toString());
            patientInfo.put("Age", this.age);
            patientInfo.put("Gender", this.gender);

            if (this.extraInfo != null && !this.extraInfo.isEmpty()) {
                patientInfo.put("Extra_Info", getExtraInfoAsMap());
            }

            return objectMapper.writeValueAsString(patientInfo);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert patient info to JSON", e);
        }
    }
}

