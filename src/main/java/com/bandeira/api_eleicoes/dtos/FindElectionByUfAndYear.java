package com.bandeira.api_eleicoes.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FindElectionByUfAndYear(

        @NotNull(message = "O uf não pode ser nulo")
        @NotBlank(message = "O uf não pode ser vazio")
        String uf,

        @NotNull(message = "O turno não pode ser nulo")
        Integer year
) {
}
