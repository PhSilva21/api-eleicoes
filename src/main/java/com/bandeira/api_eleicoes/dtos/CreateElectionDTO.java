package com.bandeira.api_eleicoes.dtos;

import com.bandeira.api_eleicoes.model.enums.ElectionTurn;
import com.bandeira.api_eleicoes.model.enums.ElectionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateElectionDTO(

        @NotNull(message = "A data da eleição não pode ser nula")
        @NotBlank(message = "A data da eleição não pode ser vazia")
        LocalDate date,

        @NotNull(message = "O uf da eleição não pode ser nulo")
        @NotBlank(message = "O uf da eleição não pode ser vazio")
        String uf,

        @NotNull(message = "O turno da eleição não pode ser nulo")
        @NotBlank(message = "O turno da eleição não pode ser vazio")
        ElectionTurn electionTurn,

        @NotNull(message = "O tipo da eleição não pode ser nulo")
        @NotBlank(message = "O tipo da eleição não pode ser vazio")
        ElectionType electionType,

        @NotNull(message = "O número de seções não pode ser nulo")
        @NotBlank(message = "O número de seções não pode ser vazio")
        Double sessions
) {
}
