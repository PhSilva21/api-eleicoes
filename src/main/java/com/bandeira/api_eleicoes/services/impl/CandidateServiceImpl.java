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
import com.bandeira.api_eleicoes.util.CandidateMapper;
import com.bandeira.api_eleicoes.util.RandomString;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
            , MultipartFile file) {
        String code = generateCandidateRegistration();

        var politicalParty = politicalPartyService.findByName(request.politicalPartyName());

        Candidate candidate = new Candidate(
                request.name(),
                SituationCandidate.FIRST_TURN,
                request.vice(),
                request.coalitionAndFederation()
        );

        candidate.setCandidateRegistration(code);
        candidate.setPoliticalParty(politicalParty);
        setPhotoPath(file, candidate);

        candidateRepository.save(candidate);

        return new CreateCandidateResponse(candidate.getName()
                , candidate.getSituationCandidate(), candidate.getVice()
                , candidate.getPoliticalParty(), candidate.getCoalitionAndFederation());
    }

    @Override
    public UpdateCandidateDTO updateCandidate(UpdateCandidateDTO request) {
        var candidate = findById(request.id());

        if(request.name() != null){
            candidate.setName(request.name());
        }
        if(request.voucherFile() != null){
            candidate.setName(request.name());
        }
        return null;
    }

    public UploadResponse setPhotoPath(MultipartFile file, Candidate candidate){
        var response = uploadService.uploadFile(file);
        candidate.setPhotoPath(response.location());
        return response;
    }

    @Override
    public Candidate findById(Long id) {
        return candidateRepository.findById(id)
                .orElseThrow(CandidateNotFoundException::new);
    }

    @Override
    public Candidate findByName(String name) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public String generateCandidateRegistration() {
        String candidateRegistration;
        do {
            candidateRegistration = RandomString.generateRandomString(6);
        } while (candidateRepository.findByCandidateRegistration(candidateRegistration) != null);

        return candidateRegistration;
    }
}
