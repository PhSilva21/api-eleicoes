package com.bandeira.api_eleicoes.dtos;

import com.bandeira.api_eleicoes.model.PoliticalParty;
import com.bandeira.api_eleicoes.model.enums.SituationCandidate;

import java.util.List;

public record UpdateCandidateDTO(

        String name,

        String voucherFile,

        SituationCandidate situationCandidate,

        String vice,

        PoliticalParty politicalParty,

        List<String> coalitionAndFederation
) {
}
