package com.bandeira.api_eleicoes.services;

import com.bandeira.api_eleicoes.dtos.AddCandidateToElectionDTO;
import com.bandeira.api_eleicoes.dtos.CloseSessionResponseDTO;
import com.bandeira.api_eleicoes.dtos.CreateElectionDTO;
import com.bandeira.api_eleicoes.dtos.FilterElectionByUfAndTurn;
import com.bandeira.api_eleicoes.model.Candidate;
import com.bandeira.api_eleicoes.model.Election;
import com.bandeira.api_eleicoes.model.PastElections;
import com.bandeira.api_eleicoes.model.enums.ElectionType;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ElectionService {

    void createElection(CreateElectionDTO request);

    void validateElection(String uf, LocalDate date, ElectionType electionType);

    @Transactional
    PastElections toPastElection(Long electionId);

    CloseSessionResponseDTO closeSession(Long electionId);

    List<Candidate> addCandidate(AddCandidateToElectionDTO request);

    Optional<Election> findByUfAndTurn(FilterElectionByUfAndTurn request);

    List<Candidate> getElectedCandidates(Long electionId);

    List<Candidate> getAllCandidates(Long electionID);

    Election findById(Long id);

    void deleteById(Long id);

    String calculation(Double value1, Double value2, Election election);
}
