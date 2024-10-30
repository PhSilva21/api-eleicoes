package com.bandeira.api_eleicoes.services;

import com.bandeira.api_eleicoes.dtos.FindElectionByUfAndYear;
import com.bandeira.api_eleicoes.dtos.UpdatePastElectionDTO;
import com.bandeira.api_eleicoes.model.Candidate;
import com.bandeira.api_eleicoes.model.PastElections;

import java.util.List;
import java.util.Optional;

public interface PastElectionService {

    Optional<PastElections> findByUfAndYear(FindElectionByUfAndYear request);

    void deleteById(Long id);

    void updatePastElection(UpdatePastElectionDTO request);

    List<Candidate> removeCandidateElected(Long id, String candidateRegistration);

    List<Candidate> addCandidateElected(Long id, String candidateRegistration);

    PastElections findById(Long id);
}
