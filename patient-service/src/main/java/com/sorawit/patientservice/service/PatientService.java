package com.sorawit.patientservice.service;

import com.sorawit.patientservice.dto.PatientRequestDTO;
import com.sorawit.patientservice.dto.PatientResponseDTO;
import com.sorawit.patientservice.exception.EmailAlreadyExistsException;
import com.sorawit.patientservice.exception.PatientNotFoundException;
import com.sorawit.patientservice.mapper.PatientMapper;
import com.sorawit.patientservice.model.Patient;
import com.sorawit.patientservice.repository.PatientRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException("patient not found with ID: {}" + id));
        if (patientRepository.existsByEmail(patientRequestDTO.email())) {
            throw new EmailAlreadyExistsException("Email already exists:" + patientRequestDTO.email());
        }

        patient.setName(patientRequestDTO.name());
        patient.setAddress(patientRequestDTO.address());
        patient.setEmail(patientRequestDTO.email());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.dateOfBirth()));

        Patient updatedPatient = patientRepository.save(patient);
        return patientMapper.toPatient(updatedPatient);
    }
}