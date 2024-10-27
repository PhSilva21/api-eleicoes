package com.bandeira.api_eleicoes.services.impl;

import com.bandeira.api_eleicoes.dtos.CreateElectionDTO;
import com.bandeira.api_eleicoes.dtos.FilterElectionByUfAndTurn;
import com.bandeira.api_eleicoes.exceptions.ElectionAlreadyExists;
import com.bandeira.api_eleicoes.exceptions.ElectionNotFound;
import com.bandeira.api_eleicoes.model.Candidate;
import com.bandeira.api_eleicoes.model.Election;
import com.bandeira.api_eleicoes.model.enums.ElectionType;
import com.bandeira.api_eleicoes.repositories.ElectionRepository;
import com.bandeira.api_eleicoes.repositories.PastElectionsRepository;
import com.bandeira.api_eleicoes.services.ElectionService;
import com.bandeira.api_eleicoes.util.ElectionMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ElectionServiceImpl implements ElectionService{

    private final ElectionRepository electionRepository;

    private final ElectionMapper electionMapper;

    private final PastElectionsRepository pastElectionsRepository;

    public ElectionServiceImpl(ElectionRepository electionRepository
            ,ElectionMapper electionMapper, PastElectionsRepository pastElectionsRepository) {
        this.electionRepository = electionRepository;
        this.electionMapper = electionMapper;
        this.pastElectionsRepository = pastElectionsRepository;
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
    public void toPastElection(Long electionId) {
        var election  = electionMapper.toPastElections(findById(electionId));

        pastElectionsRepository.save(election);

        deleteById(electionId);
    }

    @Override
    public Optional<Election> findByUfAndTurn(FilterElectionByUfAndTurn request) {
        return Optional.of(electionRepository.findAll().stream()
                .filter(e -> e.getUf().equals(request.uf())
                        && e.getElectionTurn().equals(request.electionTurn()))
                .toList().stream().findFirst()
                .orElseThrow(ElectionAlreadyExists::new));
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
}
