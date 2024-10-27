package com.bandeira.api_eleicoes.util;

import com.bandeira.api_eleicoes.dtos.CreatePoliticalPartyDTO;
import com.bandeira.api_eleicoes.dtos.PoliticalPartyResponse;
import com.bandeira.api_eleicoes.model.PoliticalParty;
import org.springframework.stereotype.Component;

@Component
public class PoliticalPartyMapper {


    public PoliticalParty toEntity(CreatePoliticalPartyDTO request){
        return new PoliticalParty(request.name(), request.uf());
    }

    public PoliticalPartyResponse toDTO(PoliticalParty politicalParty){
        return new PoliticalPartyResponse(politicalParty.getName(), politicalParty.getUf()
                , politicalParty.getCandidates());
    }
}
