package com.bandeira.api_eleicoes.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddCandidateToElectionDTO(

        @NotNull(message = "O resgistro do candidato não pode ser nulo")
        @NotBlank(message = "O registro do candidato não pode ser vazio")
        String candidateRegistration,

        @NotNull(message = "O id da eleição não pode ser nulo")
        @NotBlank(message = "O id da eleiçãp não pode ser vazio")
        Long electionId
) {
}
