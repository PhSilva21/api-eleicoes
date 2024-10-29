package com.bandeira.api_eleicoes.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePoliticalPartyDTO(

        @NotNull(message = "O nome do partido não pode ser nulo")
        @NotBlank(message = "O nome do partido não pode ser nulo")
        String name,

        @NotNull(message = "O uf do partido não pode ser nulo")
        @NotBlank(message = "O uf do partido não pode ser vazio")
        String uf

){
}
