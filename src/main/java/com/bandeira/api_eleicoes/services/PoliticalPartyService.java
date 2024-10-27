package com.bandeira.api_eleicoes.services;

import com.bandeira.api_eleicoes.dtos.CreatePoliticalPartyDTO;
import com.bandeira.api_eleicoes.dtos.PoliticalPartyResponse;
import com.bandeira.api_eleicoes.model.PoliticalParty;

import java.util.List;

public interface PoliticalPartyService {

    void createPoliticalParty(CreatePoliticalPartyDTO request);

    PoliticalParty findByName(String name);

    void validatePoliticalPartyName(String name);

    void deleteById(Long id);

    List<PoliticalPartyResponse> filterByUf(String uf);
}
