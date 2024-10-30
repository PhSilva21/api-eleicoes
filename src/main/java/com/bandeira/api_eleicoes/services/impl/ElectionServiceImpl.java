package com.bandeira.api_eleicoes.services.impl;

import com.bandeira.api_eleicoes.dtos.AddCandidateToElectionDTO;
import com.bandeira.api_eleicoes.dtos.CloseSessionResponseDTO;
import com.bandeira.api_eleicoes.dtos.CreateElectionDTO;
import com.bandeira.api_eleicoes.exceptions.ElectionAlreadyExists;
import com.bandeira.api_eleicoes.exceptions.ElectionNotFound;
import com.bandeira.api_eleicoes.model.Candidate;
import com.bandeira.api_eleicoes.model.Election;
import com.bandeira.api_eleicoes.model.PastElections;
import com.bandeira.api_eleicoes.model.enums.ElectionType;
import com.bandeira.api_eleicoes.repositories.CandidateRepository;
import com.bandeira.api_eleicoes.repositories.ElectionRepository;
import com.bandeira.api_eleicoes.repositories.PastElectionsRepository;
import com.bandeira.api_eleicoes.services.CandidateService;
import com.bandeira.api_eleicoes.services.ElectionService;
import com.bandeira.api_eleicoes.util.ElectionMapper;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ElectionServiceImpl implements ElectionService{

    private final ElectionRepository electionRepository;

    private final ElectionMapper electionMapper;

    private final PastElectionsRepository pastElectionsRepository;

    private final CandidateService candidateService;

    private final CandidateRepository candidateRepository;

    public ElectionServiceImpl(ElectionRepository electionRepository
            , ElectionMapper electionMapper, PastElectionsRepository pastElectionsRepository
            , CandidateService candidateService, CandidateRepository candidateRepository) {
        this.electionRepository = electionRepository;
        this.electionMapper = electionMapper;
        this.pastElectionsRepository = pastElectionsRepository;
        this.candidateService = candidateService;
        this.candidateRepository = candidateRepository;
    }

    @Override
    public void createElection(CreateElectionDTO request) {
        validateElection(request.uf(), request.date(), request.electionType());

        var election = electionMapper.toEntity(request);

        electionRepository.save(election);
    }
    
    @Override
    public void validateElection(String uf, LocalDate date, ElectionType electionType){
        var filterList = electionRepository.findAll().stream()
                .filter(e -> e.getUf().equals(uf)
                        && e.getDate().equals(date)
                        && e.getElectionType().equals(electionType))
                .toList();
        
        if(!filterList.isEmpty()){
            throw new ElectionAlreadyExists();
        }
    }

    @Override
    public PastElections toPastElection(Long electionId) {
        var election = findById(electionId);

        var pastElection = electionMapper.toPastElections(election);

        pastElectionsRepository.save(pastElection);

        election.getCandidates().forEach(candidate -> candidate.setElection(null));

        electionRepository.save(election);

        deleteById(electionId);

        return pastElection;
    }

    @Override
    public CloseSessionResponseDTO closeSession(Long electionId){
        var election = findById(electionId);

        election.decrementSession();

        var response = calculation(election.getTotalSessions()
                , election.getRemainingSessions(), election);

        electionRepository.save(election);

        return new CloseSessionResponseDTO(response, LocalDateTime.now());
    }

    @Override
    public List<Candidate> addCandidate(AddCandidateToElectionDTO request){
        var candidate = candidateService
                .findByCandidateRegistration(request.candidateRegistration());

        var election = findById(request.electionId());

        candidate.setElection(election);

        candidateRepository.save(candidate);

        return election.getCandidates();
    }

    @Override
    public List<Candidate> getElectedCandidates(Long electionId) {
        var election = findById(electionId);

        return election.getElectedCandidates();
    }

    @Override
    public List<Candidate> getAllCandidates(Long electionId) {
        var election = findById(electionId);

        return election.getCandidates();
    }

    @Override
    public Election findById(Long id) {
        return electionRepository.findById(id).orElseThrow(ElectionNotFound::new);
    }

    @Override
    public void deleteById(Long id){
        findById(id);
        electionRepository.deleteById(id);
    }

    @Override
    public String calculation(Double value1, Double value2, Election election){
        DecimalFormat df = new DecimalFormat("#0.00");

        var calc = (value1 * value2) / 100;

        election.setPercentageOfSectionsTotaled(calc);

        return df.format(calc);
    }
}
