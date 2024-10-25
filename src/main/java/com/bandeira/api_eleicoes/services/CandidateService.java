package com.bandeira.api_eleicoes.services;

import com.bandeira.api_eleicoes.dtos.CreateCandidateDTO;
import com.bandeira.api_eleicoes.dtos.CreateCandidateResponse;
import com.bandeira.api_eleicoes.dtos.UpdateCandidateDTO;
import com.bandeira.api_eleicoes.model.Candidate;

import java.util.Optional;

public interface CandidateService {

    CreateCandidateResponse createCandidate(CreateCandidateDTO request);

    UpdateCandidateDTO updateCandidate(UpdateCandidateDTO request);

    Candidate findById(Long id);

    Candidate findByName(String name);

    void deleteById(Long id);

    void deleteAll();

    String generateCandidateRegistration();
}
