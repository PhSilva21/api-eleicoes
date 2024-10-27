package com.bandeira.api_eleicoes.util;

import com.bandeira.api_eleicoes.dtos.CreatePoliticalPartyDTO;
import com.bandeira.api_eleicoes.model.PoliticalParty;
import org.springframework.stereotype.Component;

@Component
public class PoliticalPartyMapper {


    public PoliticalParty toEntity(CreatePoliticalPartyDTO request){
        return new PoliticalParty(request.name(), request.uf());
    }
}
