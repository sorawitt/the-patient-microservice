package com.sorawit.patientservice.mapper;

import com.sorawit.patientservice.dto.PatientRequestDTO;
import com.sorawit.patientservice.dto.PatientResponseDTO;
import com.sorawit.patientservice.model.Patient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PatientMapper {
    public PatientResponseDTO toPatient(Patient patient) {
        return new PatientResponseDTO(
                patient.getId().toString(),
                patient.getName(),
                patient.getEmail(),
                patient.getAddress(),
                patient.getDateOfBirth().toString()
        );
    }

    public Patient toPatient(PatientRequestDTO patientRequestDTO) {
        Patient patient = new Patient();
        patient.setName(patientRequestDTO.name());
        patient.setEmail(patientRequestDTO.email());
        patient.setAddress(patientRequestDTO.address());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.dateOfBirth()));
        patient.setRegisteredDate(LocalDate.parse(patientRequestDTO.registeredDate()));
        return patient;
    }
}
