package com.bandeira.api_eleicoes.services.impl;

import com.bandeira.api_eleicoes.dtos.FindElectionByUfAndYear;
import com.bandeira.api_eleicoes.dtos.UpdatePastElectionDTO;
import com.bandeira.api_eleicoes.exceptions.ElectionAlreadyExists;
import com.bandeira.api_eleicoes.exceptions.PastElectionNotFoundException;
import com.bandeira.api_eleicoes.model.Candidate;
import com.bandeira.api_eleicoes.model.PastElections;
import com.bandeira.api_eleicoes.repositories.PastElectionsRepository;
import com.bandeira.api_eleicoes.services.CandidateService;
import com.bandeira.api_eleicoes.services.PastElectionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PastElectionServiceImpl implements PastElectionService {

    private final PastElectionsRepository pastElectionsRepository;

    private final CandidateService candidateService;

    public PastElectionServiceImpl(PastElectionsRepository pastElectionsRepository
            , CandidateService candidateService) {
        this.pastElectionsRepository = pastElectionsRepository;
        this.candidateService = candidateService;
    }

    @Override
    public Optional<PastElections> findByUfAndYear(FindElectionByUfAndYear request) {
        return Optional.of(pastElectionsRepository.findAll().stream()
                .filter(e -> e.getUf().equals(request.uf())
                        && e.getDate().getYear() == request.year())
                .toList().stream().findFirst()
                .orElseThrow(ElectionAlreadyExists::new));
    }

    @Override
    public void deleteById(Long id) {
        findById(id);

        pastElectionsRepository.deleteById(id);
    }

    @Override
    public void updatePastElection(UpdatePastElectionDTO request) {
        var pastElection = findById(request.pastElectionID());

        if(request.date() != null){
            pastElection.setDate(request.date());
        }
        if(request.uf() != null){
            pastElection.setUf(request.uf());
        }
        if(request.electionTurn() != null){
            pastElection.setElectionTurn(request.electionTurn());
        }
        if(request.electionType() != null){
            pastElection.setElectionType(request.electionType());
        }
        if(request.totalSessions() != null){
            pastElection.setTotalSessions(request.totalSessions());
        }
        if(request.remainingSessions() != null){
            pastElection.setRemainingSessions(request.remainingSessions());
        }
        if(request.percentageOfSectionsTotaled() != null){
            pastElection.setPercentageOfSectionsTotaled(request.percentageOfSectionsTotaled());
        }
        if(request.latestUpdate() != null){
            pastElection.setLatestUpdate(request.latestUpdate());
        }

        pastElectionsRepository.save(pastElection);
    }

    @Override
    public List<Candidate> removeCandidateElected(Long id, String candidateRegistration) {
        var election = findById(id);

        var candidate = candidateService.findByCandidateRegistration(candidateRegistration);

        election.getElectedCandidates().remove(candidate);

        pastElectionsRepository.save(election);

        return election.getElectedCandidates();
    }

    @Override
    public List<Candidate> addCandidateElected(Long id, String candidateRegistration) {
        var election = findById(id);

        var candidate = candidateService.findByCandidateRegistration(candidateRegistration);

        election.getElectedCandidates().add(candidate);

        pastElectionsRepository.save(election);

        return election.getElectedCandidates();
    }

    @Override
    public PastElections findById(Long id) {
        return pastElectionsRepository
                .findById(id).orElseThrow(PastElectionNotFoundException::new);
    }
}
