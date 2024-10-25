package com.bandeira.api_eleicoes.repositories;

import com.bandeira.api_eleicoes.model.PoliticalParty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PoliticalPartyRepository extends JpaRepository<PoliticalParty, Long> {
}
