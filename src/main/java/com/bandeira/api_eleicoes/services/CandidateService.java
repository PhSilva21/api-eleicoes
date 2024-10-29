package com.bandeira.api_eleicoes.services;

import com.bandeira.api_eleicoes.dtos.CreateCandidateDTO;
import com.bandeira.api_eleicoes.dtos.CreateCandidateResponse;
import com.bandeira.api_eleicoes.dtos.UpdateCandidateDTO;
import com.bandeira.api_eleicoes.dtos.UploadResponse;
import com.bandeira.api_eleicoes.model.Candidate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface CandidateService {

    CreateCandidateResponse createCandidate(CreateCandidateDTO request, MultipartFile file) throws Exception;

    void updateCandidate(UpdateCandidateDTO request, MultipartFile file) throws Exception;

    Candidate findByCandidateRegistration(String code);

    Candidate findById(Long id);

    Candidate findByName(String name);

    void deleteById(Long id);

    UploadResponse setPhotoPath(MultipartFile file, Candidate candidate) throws Exception;

    String generateCandidateRegistration();
}
