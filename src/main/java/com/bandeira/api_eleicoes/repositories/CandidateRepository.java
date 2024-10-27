package com.bandeira.api_eleicoes.repositories;

import com.bandeira.api_eleicoes.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    Optional<Candidate> findByCandidateRegistration(String code);

    Optional<Candidate> findByName(String name);
}
