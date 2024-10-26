package com.bandeira.api_eleicoes.repositories;

import com.bandeira.api_eleicoes.model.PastElections;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PastElectionsRepository extends JpaRepository<PastElections, Long> {
}
