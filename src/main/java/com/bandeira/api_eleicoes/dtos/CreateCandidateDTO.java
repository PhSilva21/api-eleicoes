package com.bandeira.api_eleicoes.dtos;

import com.bandeira.api_eleicoes.model.PoliticalParty;
import com.bandeira.api_eleicoes.model.enums.SituationCandidate;

public record CreateCandidateDTO(

        String name,

        SituationCandidate situationCandidate,

        String vice,

        String politicalPartyName,

        String coalitionAndFederation

) {
}
