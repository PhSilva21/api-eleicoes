package com.bandeira.api_eleicoes.services.impl;

import com.bandeira.api_eleicoes.dtos.CreateCandidateDTO;
import com.bandeira.api_eleicoes.dtos.CreateCandidateResponse;
import com.bandeira.api_eleicoes.dtos.UpdateCandidateDTO;
import com.bandeira.api_eleicoes.dtos.UploadResponse;
import com.bandeira.api_eleicoes.exceptions.CandidateNotFoundException;
import com.bandeira.api_eleicoes.model.Candidate;
import com.bandeira.api_eleicoes.model.enums.SituationCandidate;
import com.bandeira.api_eleicoes.repositories.CandidateRepository;
import com.bandeira.api_eleicoes.services.CandidateService;
import com.bandeira.api_eleicoes.services.PoliticalPartyService;
import com.bandeira.api_eleicoes.services.UploadService;
import com.bandeira.api_eleicoes.util.RandomString;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;

    private final PoliticalPartyService politicalPartyService;

    private final UploadService uploadService;

    public CandidateServiceImpl(CandidateRepository candidateRepository
            , PoliticalPartyService politicalPartyService, UploadService uploadService) {
        this.candidateRepository = candidateRepository;
        this.politicalPartyService = politicalPartyService;
        this.uploadService = uploadService;
    }

    @Override
    public CreateCandidateResponse createCandidate(CreateCandidateDTO request
            , MultipartFile file) throws Exception {
        String code = generateCandidateRegistration();

        var politicalParty = politicalPartyService.findByName(request.politicalPartyName());

        Candidate candidate = new Candidate(
                request.name(),
                code,
                SituationCandidate.FIRST_TURN,
                politicalParty,
                request.vice(),
                request.coalitionAndFederation()
        );

        setPhotoPath(file, candidate);

        return new CreateCandidateResponse(candidate.getName()
                , candidate.getSituationCandidate(), candidate.getVice()
                , candidate.getPoliticalParty(), candidate.getCoalitionAndFederation());
    }

    @Override
    public void updateCandidate(UpdateCandidateDTO request, MultipartFile file) throws Exception {
        var candidate = findById(request.id());

        if(request.name() != null){
            candidate.setName(request.name());
        }
        if(file != null){
            setPhotoPath(file, candidate);
        }
        if(request.situationCandidate() != null){
            candidate.setSituationCandidate(request.situationCandidate());
        }
        if(request.vice() != null){
            candidate.setVice(request.vice());
        }
        if(request.politicalPartyName() != null){
            candidate.setPoliticalParty(politicalPartyService
                    .findByName(request.politicalPartyName()));
        }
        if(request.coalitionAndFederation() != null){
            candidate.setCoalitionAndFederation(request.coalitionAndFederation());
        }
        candidateRepository.save(candidate);
    }

    @Override
    public UploadResponse setPhotoPath(MultipartFile file, Candidate candidate) throws Exception {
        var response = uploadService.uploadFile(file);
        candidate.setPhotoPath(response.location());

        candidateRepository.save(candidate);

        return response;
    }

    @Override
    public String generateCandidateRegistration() {
        String candidateRegistration;
        do {
            candidateRegistration = RandomString.generateRandomString(6);
        } while (candidateRepository.findByCandidateRegistration(candidateRegistration).isPresent());

        return candidateRegistration;
    }

    @Override
    public Candidate findByCandidateRegistration(String code) {
        return candidateRepository.findByCandidateRegistration(code)
                .orElseThrow(CandidateNotFoundException::new);
    }

    @Override
    public Candidate findById(Long id) {
        return candidateRepository.findById(id)
                .orElseThrow(CandidateNotFoundException::new);
    }

    @Override
    public Candidate findByName(String name) {
        return candidateRepository.findByName(name)
                .orElseThrow(CandidateNotFoundException::new);
    }

    @Override
    public void deleteById(Long id) {
        findById(id);
        candidateRepository.deleteById(id);
    }

}
