package com.cpxHelper.myPatients.domain.repository;

import com.cpxHelper.myPatients.domain.entity.patient.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
}

