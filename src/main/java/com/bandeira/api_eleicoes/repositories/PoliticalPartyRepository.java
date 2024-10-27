package com.bandeira.api_eleicoes.repositories;

import com.bandeira.api_eleicoes.model.PoliticalParty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PoliticalPartyRepository extends JpaRepository<PoliticalParty, Long> {


    Optional<PoliticalParty> findByName(String name);
}
