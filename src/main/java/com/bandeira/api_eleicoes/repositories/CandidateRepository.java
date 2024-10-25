package com.bandeira.api_eleicoes.repositories;

import com.bandeira.api_eleicoes.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    Candidate findByCandidateRegistration(String code);
}
