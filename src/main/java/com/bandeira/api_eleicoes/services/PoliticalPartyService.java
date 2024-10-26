package com.bandeira.api_eleicoes.services;

import com.bandeira.api_eleicoes.model.PoliticalParty;

public interface PoliticalPartyService {

    PoliticalParty findByName(String name);
}
