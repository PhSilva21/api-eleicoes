package com.bandeira.api_eleicoes.dtos;

import com.bandeira.api_eleicoes.model.enums.ElectionTurn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FilterElectionByUfAndTurn(

        @NotNull(message = "O uf não pode ser nulo")
        @NotBlank(message = "O uf não pode ser vazio")
        String uf,

        @NotNull(message = "O turno não pode ser nulo")
        ElectionTurn electionTurn
) {
}
