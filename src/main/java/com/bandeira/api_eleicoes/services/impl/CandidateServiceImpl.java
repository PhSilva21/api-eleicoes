package com.bandeira.api_eleicoes.services.impl;

import com.bandeira.api_eleicoes.dtos.CreateCandidateDTO;
import com.bandeira.api_eleicoes.dtos.CreateCandidateResponse;
import com.bandeira.api_eleicoes.dtos.UpdateCandidateDTO;
import com.bandeira.api_eleicoes.model.Candidate;
import com.bandeira.api_eleicoes.repositories.CandidateRepository;
import com.bandeira.api_eleicoes.services.CandidateService;
import com.bandeira.api_eleicoes.services.util.CandidateMapper;
import com.bandeira.api_eleicoes.services.util.RandomString;

public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;

    private final PoliticalPartyService politicalPartyService;

    public CandidateServiceImpl(CandidateRepository candidateRepository
            , PoliticalPartyService politicalPartyService) {
        this.candidateRepository = candidateRepository;
        this.politicalPartyService = politicalPartyService;
    }

    @Override
    public CreateCandidateResponse createCandidate(CreateCandidateDTO request) {
        String code = generateCandidateRegistration();

        var politicalParty = politicalPartyService.findByName(request.politicalPartyName());

        Candidate candidate = CandidateMapper.toEntity(request);

        candidate.setCandidateRegistration(code);
        candidate.setPoliticalParty(politicalParty);

        candidateRepository.save(candidate);

        return new CreateCandidateResponse(candidate.getName()
                , candidate.getSituationCandidate(), candidate.getVice()
                , candidate.getPoliticalParty(), candidate.getCoalitionAndFederation());
    }

    @Override
    public UpdateCandidateDTO updateCandidate(UpdateCandidateDTO request) {
        return null;
    }

    @Override
    public Candidate findById(Long id) {
        return null;
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
