package com.sorawit.patientservice.service;

import com.sorawit.patientservice.dto.PatientRequestDTO;
import com.sorawit.patientservice.dto.PatientResponseDTO;
import com.sorawit.patientservice.exception.EmailAlreadyExistsException;
import com.sorawit.patientservice.mapper.PatientMapper;
import com.sorawit.patientservice.model.Patient;
import com.sorawit.patientservice.repository.PatientRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public PatientService(PatientRepository patientRepository, PatientMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
    }

    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(patientMapper::toPatient).toList();
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        if (patientRepository.existsByEmail(patientRequestDTO.email())) {
            throw new EmailAlreadyExistsException("Email already exists:" + patientRequestDTO.email());
        }
        Patient newPatient = patientMapper.toPatient(patientRequestDTO);
        patientRepository.save(newPatient);

        return patientMapper.toPatient(newPatient);
    }
}