package com.bandeira.api_eleicoes.dtos;

import com.bandeira.api_eleicoes.model.PoliticalParty;
import com.bandeira.api_eleicoes.model.enums.SituationCandidate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdateCandidateDTO(

        @NotNull(message = "O id não pode ser nulo")
        Long id,

        String name,

        SituationCandidate situationCandidate,

        String vice,

        String politicalPartyName,

        String coalitionAndFederation
) {
}
