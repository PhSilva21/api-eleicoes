package com.bandeira.api_eleicoes.repositories;

import com.bandeira.api_eleicoes.model.Election;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ElectionRepository extends JpaRepository<Election, Long> {
}
