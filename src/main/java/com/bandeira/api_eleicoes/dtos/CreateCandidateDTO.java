package com.bandeira.api_eleicoes.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCandidateDTO(

        @NotNull(message = "O nome do candidato não pode ser nulo")
        @NotBlank(message = "O nome do candidato não pode ser vazio")
        String name,

        @NotNull(message = "O vice do candidato não pode ser nulo")
        @NotBlank(message = "O vice do candidato não pode ser vazio")
        String vice,

        @NotNull(message = "O partido do candidato não pode ser nulo")
        @NotBlank(message = "O partido do candidato não pode ser vazio")
        String politicalPartyName,

        String coalitionAndFederation

) {
}
