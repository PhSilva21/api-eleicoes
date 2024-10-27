package com.bandeira.api_eleicoes.services;

import com.bandeira.api_eleicoes.dtos.CreateCandidateDTO;
import com.bandeira.api_eleicoes.dtos.CreateCandidateResponse;
import com.bandeira.api_eleicoes.dtos.UpdateCandidateDTO;
import com.bandeira.api_eleicoes.model.Candidate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface CandidateService {

    CreateCandidateResponse createCandidate(CreateCandidateDTO request, MultipartFile file);

    void updateCandidate(UpdateCandidateDTO request, MultipartFile file);

    Candidate findById(Long id);

    Candidate findByName(String name);

    void deleteById(Long id);


    String generateCandidateRegistration();
}
